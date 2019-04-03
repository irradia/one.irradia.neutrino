package one.irradia.neutrino.views

import java.io.Serializable

/**
 * The arguments required to instantiate a page that looks at a feed.
 */

data class NPageFeedArguments(
  val depth: Int,
  val title: String,
  val isExternalCollection: Boolean)
  : Serializable
