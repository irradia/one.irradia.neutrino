package one.irradia.neutrino.views

import io.reactivex.subjects.Subject

interface NeutrinoListenerType {

  val neutrinoEventBus: Subject<NeutrinoEventType>

  fun onNeutrinoTabSelected(
    tab: NeutrinoTabType)

  fun onNeutrinoTabUpdated(
    tab: NeutrinoTabType)

  fun onNeutrinoPageMenuUpdated(
    page: NeutrinoPageType)

}
