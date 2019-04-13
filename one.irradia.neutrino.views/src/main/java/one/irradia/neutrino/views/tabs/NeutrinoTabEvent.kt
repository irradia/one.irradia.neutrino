package one.irradia.neutrino.views.tabs

import one.irradia.neutrino.views.api.NeutrinoEventType

sealed class NeutrinoTabEvent : NeutrinoEventType {

  data class TabPageStackChanged(
    val tab: NeutrinoTabType)
    : NeutrinoTabEvent()

}