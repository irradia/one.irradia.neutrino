package one.irradia.neutrino.feeds.api

import one.irradia.neutrino.feeds.api.FeedLoadResult.FeedLoadFailure.FeedLoadFailureParse
import one.irradia.neutrino.feeds.api.FeedLoadResult.FeedLoadFailure.FeedLoadFailureTransport.FeedLoadFailureTransportGeneric
import one.irradia.neutrino.feeds.api.FeedLoadResult.FeedLoadFailure.FeedLoadFailureTransport.FeedLoadFailureTransportHTTP

/**
 * The result of loading a feed.
 */

sealed class FeedLoadResult<T> {

  companion object {

    /**
     * Monadic bind for results.
     */

    fun <A, B> flatMap(x: FeedLoadResult<A>, f: (A) -> FeedLoadResult<B>): FeedLoadResult<B> =
      when (x) {
        is FeedLoadSuccess ->
          when (val result = f(x.result)) {
            is FeedLoadSuccess ->
              FeedLoadSuccess(
                warnings = x.warnings.plus(result.warnings),
                result = result.result)

            is FeedLoadFailureParse ->
              FeedLoadFailureParse(
                warnings = x.warnings.plus(result.warnings),
                errors = result.errors)

            is FeedLoadFailureTransportGeneric ->
              result

            is FeedLoadFailureTransportHTTP ->
              result
          }

        is FeedLoadFailureParse ->
          x as FeedLoadResult<B>
        is FeedLoadFailureTransportGeneric ->
          x as FeedLoadResult<B>
        is FeedLoadFailureTransportHTTP ->
          x as FeedLoadResult<B>
      }
  }

  /**
   * Monadic bind for results.
   */

  fun <U> flatMap(f: (T) -> FeedLoadResult<U>): FeedLoadResult<U> =
    Companion.flatMap(this, f)

  /**
   * Feed loading succeeded.
   */

  data class FeedLoadSuccess<T>(
    val warnings: List<FeedParseWarning> = listOf(),
    val result: T)
    : FeedLoadResult<T>()

  /**
   * Feed loading failed.
   */

  sealed class FeedLoadFailure<T> : FeedLoadResult<T>() {

    /**
     * Data was received, but couldn't be parsed as a usable feed.
     */

    data class FeedLoadFailureParse<T>(
      val warnings: List<FeedParseWarning> = listOf(),
      val errors: List<FeedParseError> = listOf())
      : FeedLoadFailure<T>()

    /**
     * Data was not received due to some problem with the underlying transport mechanism.
     */

    sealed class FeedLoadFailureTransport<T> : FeedLoadFailure<T>() {

      /**
       * A generic error occurred. This typically indicates a networking issue such as being
       * unable to connect to a remote server.
       */

      data class FeedLoadFailureTransportGeneric<T>(
        val exception: Exception)
        : FeedLoadFailureTransport<T>()

      /**
       * A HTTP-specific error occurred.
       */

      data class FeedLoadFailureTransportHTTP<T>(
        val message: String,
        val statusCode: Int,
        val exception: Exception?)
        : FeedLoadFailureTransport<T>()

    }
  }

}
