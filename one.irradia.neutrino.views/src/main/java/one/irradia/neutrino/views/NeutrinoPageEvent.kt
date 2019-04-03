package one.irradia.neutrino.views

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