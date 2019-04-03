package one.irradia.neutrino.views

import android.content.Context

/**
 * The catalog tab.
 */

class NCatalogTab(
  context: Context,
  private val listener: NeutrinoListenerType)
  : NPageStackTab(rootPage = rootPageConstructor(context), listener = listener),
  NeutrinoTabType {

  init {
    val subscription =
      this.listener.neutrinoEventBus.ofType(NCatalogEvent::class.java)
        .subscribe { event -> this.onCatalogEvent(event) }
  }

  companion object {
    private fun rootPageConstructor(context: Context): NPageConstructor {
      return NPageConstructor {
        NPageFeed.create(NPageFeedArguments(
          depth = 0,
          isExternalCollection = true,
          title = context.getString(R.string.neutrino_tab_catalog)))
      }
    }
  }

  private val pageCollectionSelect: NCatalogPageCollectionSelect = NCatalogPageCollectionSelect()

  private fun onCatalogEvent(event: NCatalogEvent) =
    when (event) {
      NCatalogEvent.NCatalogCollectionSelectOpenRequested ->
        this.pagePush(this.pageCollectionSelect)
      is NCatalogEvent.NCatalogFeedRequested ->
        this.pagePush(NPageFeed.create(event.arguments))
    }
}

