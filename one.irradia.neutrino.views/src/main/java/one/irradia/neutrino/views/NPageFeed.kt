package one.irradia.neutrino.views

import android.os.Bundle
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.Fragment
import one.irradia.neutrino.views.pages.NPageAbstract
import one.irradia.neutrino.views.pages.NPageConstructor
import one.irradia.neutrino.views.pages.NeutrinoPageEvent
import one.irradia.neutrino.views.pages.NeutrinoPageType
import android.R.menu
import androidx.appcompat.view.menu.MenuBuilder


class NPageFeed : NPageAbstract(), NeutrinoPageType {

  override fun pageSaveState(): NPageConstructor? {
    val arguments = this.feedArguments
    return if (arguments != null) {
      constructor(arguments)
    } else {
      null
    }
  }

  companion object {

    fun constructor(arguments: NPageFeedArguments): NPageConstructor =
      NPageConstructor { create(arguments) }

    fun create(arguments: NPageFeedArguments): NPageFeed {
      val result = NPageFeed()
      val bundle = Bundle()
      bundle.putSerializable("one.irradia.neutrino.views.NPageFeed.arguments", arguments)
      result.arguments = bundle
      return result
    }
  }

  private var feedArguments: NPageFeedArguments? = null

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)

    this.feedArguments =
      this.arguments!!.get("one.irradia.neutrino.views.NPageFeed.arguments")
        as NPageFeedArguments

    if (this.feedArguments!!.isExternalCollection) {
      this.setHasOptionsMenu(true)
    }
  }

  override fun onCreateOptionsMenu(
    menu: Menu,
    inflater: MenuInflater) {
    super.onCreateOptionsMenu(menu, inflater)

    if (this.feedArguments!!.isExternalCollection) {
      inflater.inflate(R.menu.neutrino_page_feed_external_menu, menu)

      val itemSwitch = menu.findItem(R.id.neutrinoCatalogMenuSwitchCollection)
      itemSwitch.setOnMenuItemClickListener {
        this.listener.neutrinoEventBus.onNext(
          NeutrinoPageEvent.OpenPageOnCurrentTab(
            callingPage = this,
            constructor = NCatalogPageCollectionSelect.constructor()))
        true
      }

      val itemHome = menu.findItem(R.id.neutrinoCatalogMenuRoot)
      itemHome.setOnMenuItemClickListener {
        true
      }
    }
  }

  override fun pageTitle(): String? {
    return this.feedArguments?.let { arguments -> "${arguments.title}: ${arguments.depth}" }
  }

  override fun pageFragment(): Fragment {
    return this
  }

  override fun onCreateView(
    inflater: LayoutInflater,
    container: ViewGroup?,
    savedInstanceState: Bundle?): View? {
    super.onCreateView(inflater, container, savedInstanceState)

    val currentArguments = this.feedArguments!!
    return if (currentArguments.isExternalCollection) {
      val view = inflater.inflate(R.layout.neutrino_page_feed, container, false)
      val button = view.findViewById<Button>(R.id.neutrinoFeedNext)
      val nextDepth = currentArguments.depth + 1
      button.setText("Next: $nextDepth")
      button.setOnClickListener {
        this.listener.neutrinoEventBus.onNext(
          NeutrinoPageEvent.OpenPageOnCurrentTab(
            callingPage = this,
            constructor = NPageFeed.constructor(currentArguments.copy(depth = nextDepth))))
      }
      view
    } else {
      inflater.inflate(R.layout.neutrino_nothing, container, false)
    }
  }
}