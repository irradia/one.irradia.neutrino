package one.irradia.neutrino.views

import java.io.Serializable

/**
 * The type of serializable functions that can create pages on demand.
 */

data class NPageConstructor(
  val constructor: () -> NeutrinoPageType)
  : Serializable
