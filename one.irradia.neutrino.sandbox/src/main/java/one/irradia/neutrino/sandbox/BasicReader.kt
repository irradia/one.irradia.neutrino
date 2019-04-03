package one.irradia.neutrino.sandbox

import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import io.reactivex.subjects.Subject
import one.irradia.neutrino.views.NeutrinoActivityHelper
import one.irradia.neutrino.views.NeutrinoEventType
import one.irradia.neutrino.views.NeutrinoListenerType
import one.irradia.neutrino.views.NeutrinoMain
import one.irradia.neutrino.views.NeutrinoPageType
import one.irradia.neutrino.views.NeutrinoTabType

class BasicReader : AppCompatActivity(), NeutrinoListenerType {

  override val neutrinoEventBus: Subject<NeutrinoEventType>
    get() = this.neutrinoMain.eventBus

  private lateinit var neutrinoMain: NeutrinoMain

  override fun onNeutrinoTabPageStackChanged(tab: NeutrinoTabType) =
    NeutrinoActivityHelper.onNeutrinoTabPageStackChanged(this, tab)

  override fun onNeutrinoPageMenuUpdated(page: NeutrinoPageType) =
    NeutrinoActivityHelper.onNeutrinoPageMenuUpdated(this, page)

  override fun onNeutrinoTabSelected(tab: NeutrinoTabType) =
    NeutrinoActivityHelper.onNeutrinoTabSelected(this, tab)

  override fun onBackPressed() {
    NeutrinoActivityHelper.onNeutrinoBackPressed(this)
  }

  override fun onOptionsItemSelected(item: MenuItem): Boolean =
    NeutrinoActivityHelper.onNeutrinoOptionsItemSelected(this, item)

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    this.neutrinoMain = NeutrinoActivityHelper.onNeutrinoCreate(this, savedInstanceState)
  }
}