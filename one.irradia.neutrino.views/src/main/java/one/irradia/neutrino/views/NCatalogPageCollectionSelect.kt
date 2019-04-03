package one.irradia.neutrino.views

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import java.io.Serializable

class NCatalogPageCollectionSelect : NPageAbstract() {

  override fun pageSaveState(): NPageConstructor =
    constructor()

  data class Parameters(
    val unused: Int) : Serializable

  companion object {

    fun constructor(): NPageConstructor =
      NPageConstructor { create(Parameters(unused = 23)) }

    fun create(parameters: Parameters): NCatalogPageCollectionSelect {
      val page = NCatalogPageCollectionSelect()
      val bundle = Bundle()
      bundle.putSerializable("one.irradia.neutrino.views.NCatalogPageCollectionSelect", parameters)
      page.arguments = bundle
      return page
    }
  }

  override fun pageTitle(): String? {
    return context?.resources?.getString(R.string.neutrino_collection_select)
  }

  override fun pageFragment(): Fragment {
    return this
  }

  override fun onCreateView(
    inflater: LayoutInflater,
    container: ViewGroup?,
    savedInstanceState: Bundle?): View? {
    super.onCreateView(inflater, container, savedInstanceState)
    return inflater.inflate(R.layout.neutrino_catalog_page_collection_select, container, false)
  }
}