package one.irradia.neutrino.views.pages

import java.io.Serializable

/**
 * The type of serializable functions that can create pages on demand.
 */

data class NPageConstructor(
  val constructor: () -> NeutrinoPageType)
  : Serializable
