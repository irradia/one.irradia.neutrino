package one.irradia.neutrino.views

import io.reactivex.subjects.Subject

interface NeutrinoListenerType {

  val neutrinoEventBus: Subject<NeutrinoEventType>

  fun onNeutrinoTabPageStackChanged(
    tab: NeutrinoTabType)

  fun onNeutrinoPageMenuUpdated(
    page: NeutrinoPageType)

  fun onNeutrinoTabSelected(
    tab: NeutrinoTabType)

}
