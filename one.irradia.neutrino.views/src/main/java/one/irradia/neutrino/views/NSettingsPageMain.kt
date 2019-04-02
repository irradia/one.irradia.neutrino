package one.irradia.neutrino.views

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment

class NSettingsPageMain : NPageAbstract(), NeutrinoPageType {

  override fun pageTitle(): String? {
    return context?.resources?.getString(R.string.neutrino_tab_settings)
  }

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
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

    val view =
      inflater.inflate(R.layout.neutrino_settings_page_main, container, false)
    return view
  }
}