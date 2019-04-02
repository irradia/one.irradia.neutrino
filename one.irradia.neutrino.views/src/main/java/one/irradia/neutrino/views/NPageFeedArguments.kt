package one.irradia.neutrino.views

import java.io.Serializable

data class NPageFeedArguments(
  val depth: Int,
  val title: String,
  val isExternalCollection: Boolean)
  : Serializable
