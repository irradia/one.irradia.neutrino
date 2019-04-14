package one.irradia.neutrino.feeds.api

/**
 * The result of parsing a document.
 */

sealed class FeedParseResult<T> {

  companion object {

    /**
     * Monadic bind for results.
     */

    fun <A, B> flatMap(r: FeedParseResult<A>, f: (A) -> FeedParseResult<B>): FeedParseResult<B> =
      when (r) {
        is FeedParseSuccess ->
          when (val u = f.invoke(r.result)) {
            is FeedParseSuccess ->
              FeedParseSuccess(
                warnings = r.warnings.plus(u.warnings),
                result = u.result)

            is FeedParseFailure ->
              FeedParseFailure(
                warnings = r.warnings.plus(u.warnings),
                errors = u.errors)
          }

        is FeedParseFailure ->
          FeedParseFailure(
            warnings = r.warnings,
            errors = r.errors)
      }
  }

  /**
   * Monadic bind for results.
   */

  fun <U> flatMap(
    f: (T) -> FeedParseResult<U>): FeedParseResult<U> =
    Companion.flatMap(this, f)

  /**
   * Parsing succeeded.
   */

  data class FeedParseSuccess<T>(

    /**
     * The warnings encountered.
     */

    val warnings: List<FeedParseWarning> = listOf(),

    /**
     * The parsed value.
     */

    val result: T) : FeedParseResult<T>()

  /**
   * Parsing failed.
   */

  data class FeedParseFailure<T>(

    /**
     * The warnings encountered.
     */

    val warnings: List<FeedParseWarning> = listOf(),

    /**
     * The list of parse errors.
     */

    val errors: List<FeedParseError>) : FeedParseResult<T>()

}
