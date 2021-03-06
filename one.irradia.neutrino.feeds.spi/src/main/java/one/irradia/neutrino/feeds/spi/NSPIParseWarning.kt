package one.irradia.neutrino.feeds.spi

/**
 * A specific parse warning.
 */

data class NSPIParseWarning(

  /**
   * The parser that produced the warning. This will typically be the name of the core parser, or
   * the name of one of the extension parsers.
   */

  val producer: String,

  /**
   * Lexical information for the parse warning.
   */

  val lexical: NSPILexicalPosition,

  /**
   * The warning message.
   */

  val message: String,

  /**
   * The exception raised, if any.
   */

  val exception: Exception?)