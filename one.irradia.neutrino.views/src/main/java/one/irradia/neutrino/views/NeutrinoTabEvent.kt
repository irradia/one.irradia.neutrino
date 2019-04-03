package one.irradia.neutrino.views

sealed class NeutrinoTabEvent : NeutrinoEventType {

  data class TabPageStackChanged(
    val tab: NeutrinoTabType)
    : NeutrinoTabEvent()

}