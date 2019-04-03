package one.irradia.neutrino.views

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ProgressBar
import androidx.annotation.UiThread
import androidx.fragment.app.Fragment
import java.io.Serializable

class NSettingsPageMain : NPageAbstract(), NeutrinoPageType {

  override fun pageSaveState(): NPageConstructor =
    NPageConstructor { create(Parameters(unused = 23)) }

  data class Parameters(
    val unused: Int): Serializable

  companion object {
    fun create(parameters: Parameters): NSettingsPageMain {
      val page = NSettingsPageMain()
      val bundle = Bundle()
      bundle.putSerializable("one.irradia.neutrino.views.NSettingsPageMain.parameters", parameters)
      page.arguments = bundle
      return page
    }
  }

  override fun pageTitle(): String? {
    return context?.resources?.getString(R.string.neutrino_tab_settings)
  }

  override fun onStart() {
    super.onStart()
    this.listener.onNeutrinoPageMenuUpdated(this)
  }

  override fun pageFragment(): Fragment {
    return this
  }

  override fun onCreateView(
    inflater: LayoutInflater,
    container: ViewGroup?,
    savedInstanceState: Bundle?): View? {
    super.onCreateView(inflater, container, savedInstanceState)

    val view = inflater.inflate(R.layout.neutrino_settings_page_main, container, false)

    val progress =
      view.findViewById<ProgressBar>(R.id.neutrinoSettingsPageProgress)
    val button =
      view.findViewById<Button>(R.id.neutrinoSettingsPageButton)

    button.setOnClickListener {
      progress.visibility = View.VISIBLE
      Handler().postDelayed({
        progress.visibility = View.INVISIBLE
        this.listener.neutrinoEventBus.onNext(NCatalogEvent.NCatalogCollectionSelectOpenRequested)
      }, 2_000)
    }

    return view
  }
}