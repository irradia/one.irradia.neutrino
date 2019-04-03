package one.irradia.neutrino.views

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment

class NCatalogPageCollectionSelect : NPageAbstract() {

  override fun pageTitle(): String? {
    return context?.resources?.getString(R.string.neutrino_collection_select)
  }

  override fun pageFragment(): Fragment {
    return this
  }

  override fun onStart() {
    super.onStart()
    this.listener.onNeutrinoPageMenuUpdated(this)
  }

  override fun onCreateView(
    inflater: LayoutInflater,
    container: ViewGroup?,
    savedInstanceState: Bundle?): View? {
    super.onCreateView(inflater, container, savedInstanceState)
    return inflater.inflate(R.layout.neutrino_catalog_page_collection_select, container, false)
  }
}