package one.irradia.neutrino.sandbox

import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import io.reactivex.subjects.Subject
import one.irradia.neutrino.views.NeutrinoActivityAdapter
import one.irradia.neutrino.views.NeutrinoEventType
import one.irradia.neutrino.views.NeutrinoListenerType
import one.irradia.neutrino.views.NeutrinoPageType
import one.irradia.neutrino.views.NeutrinoTabType

class BasicReader : AppCompatActivity(), NeutrinoListenerType {

  private lateinit var adapter: NeutrinoActivityAdapter

  override val neutrinoEventBus: Subject<NeutrinoEventType>
    get() = this.adapter.neutrinoEventBus

  override fun onNeutrinoTabPageStackChanged(tab: NeutrinoTabType) =
    this.adapter.onNeutrinoTabPageStackChanged(tab)

  override fun onNeutrinoPageMenuUpdated(page: NeutrinoPageType) =
    this.adapter.onNeutrinoPageMenuUpdated(page)

  override fun onNeutrinoTabSelected(tab: NeutrinoTabType) =
    this.adapter.onNeutrinoTabSelected(tab)

  override fun onBackPressed() {
    if (!this.adapter.onNeutrinoBackPressed()) {
      return super.onBackPressed()
    }
  }

  override fun onDestroy() {
    super.onDestroy()
    this.adapter.onNeutrinoDestroy()
  }

  override fun onOptionsItemSelected(item: MenuItem): Boolean {
    return if (!this.adapter.onNeutrinoOptionsItemSelected(item)) {
      super.onOptionsItemSelected(item)
    } else {
      true
    }
  }

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    this.adapter = NeutrinoActivityAdapter(this)
    this.adapter.onNeutrinoCreate(savedInstanceState)
  }
}