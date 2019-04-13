package one.irradia.neutrino.views.api

import io.reactivex.subjects.Subject
import one.irradia.neutrino.views.pages.NeutrinoPageType
import one.irradia.neutrino.views.tabs.NeutrinoTabType

interface NeutrinoListenerType {

  val neutrinoEventBus: Subject<NeutrinoEventType>

  fun onNeutrinoTabSelected(
    tab: NeutrinoTabType)

  fun onNeutrinoTabUpdated(
    tab: NeutrinoTabType)

  fun onNeutrinoPageMenuUpdated(
    page: NeutrinoPageType)

}
