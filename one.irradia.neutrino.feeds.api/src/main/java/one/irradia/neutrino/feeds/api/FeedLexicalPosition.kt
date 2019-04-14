package one.irradia.neutrino.feeds.api

import java.net.URI

/**
 * A lexical position of an element in a document.
 */

data class FeedLexicalPosition(

  /**
   * The source URI of the document.
   */

  val source: URI,

  /**
   * The line number.
   */

  val line: Int,

  /**
   * The column number.
   */

  val column: Int)
