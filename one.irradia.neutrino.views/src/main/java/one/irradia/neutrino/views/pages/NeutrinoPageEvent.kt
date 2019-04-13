package one.irradia.neutrino.views.pages

import one.irradia.neutrino.views.api.NeutrinoEventType
import one.irradia.neutrino.views.tabs.NeutrinoTabType

sealed class NeutrinoPageEvent : NeutrinoEventType {

  data class OpenPageOnCurrentTab(
    val callingPage: NeutrinoPageType,
    val constructor: NPageConstructor)
    : NeutrinoPageEvent()

  data class OpenPageOnSpecificTab(
    val callingPage: NeutrinoPageType,
    val tab: Class<out NeutrinoTabType>,
    val constructor: NPageConstructor)
    : NeutrinoPageEvent()

}