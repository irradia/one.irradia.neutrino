package one.irradia.neutrino.feeds.spi

/**
 * The result of parsing a document.
 */

sealed class NSPIFeedParseResult<T> {

  companion object {

    /**
     * Monadic bind for results.
     */

    fun <A, B> flatMap(r: NSPIFeedParseResult<A>, f: (A) -> NSPIFeedParseResult<B>): NSPIFeedParseResult<B> =
      when (r) {
        is NSPIParseSucceeded ->
          when (val u = f.invoke(r.result)) {
            is NSPIParseSucceeded ->
              NSPIParseSucceeded(
                warnings = r.warnings.plus(u.warnings),
                result = u.result)

            is NSPIParseFailed ->
              NSPIParseFailed(
                warnings = r.warnings.plus(u.warnings),
                errors = u.errors)
          }

        is NSPIParseFailed ->
          NSPIParseFailed(
            warnings = r.warnings,
            errors = r.errors)
      }
  }

  /**
   * Monadic bind for results.
   */

  fun <U> flatMap(
    f: (T) -> NSPIFeedParseResult<U>): NSPIFeedParseResult<U> =
    Companion.flatMap(this, f)

  /**
   * Parsing succeeded.
   */

  data class NSPIParseSucceeded<T>(

    /**
     * The warnings encountered.
     */

    val warnings: List<NSPIParseWarning> = listOf(),

    /**
     * The parsed value.
     */

    val result: T) : NSPIFeedParseResult<T>()

  /**
   * Parsing failed.
   */

  data class NSPIParseFailed<T>(

    /**
     * The warnings encountered.
     */

    val warnings: List<NSPIParseWarning> = listOf(),

    /**
     * The list of parse errors.
     */

    val errors: List<NSPIParseError>) : NSPIFeedParseResult<T>()

}
