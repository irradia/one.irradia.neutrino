package one.irradia.neutrino.feeds.model

import java.net.URI

/**
 * A feed.
 */

data class NeutrinoFeed(

  /**
   * The feed URI
   */

  val uri: URI,

  /**
   * The feed title
   */

  val title: String)
