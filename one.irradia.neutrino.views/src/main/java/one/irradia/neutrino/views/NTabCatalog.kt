package one.irradia.neutrino.views

import android.content.Context
import one.irradia.neutrino.views.api.NeutrinoListenerType
import one.irradia.neutrino.views.pages.NPageConstructor
import one.irradia.neutrino.views.tabs.NTabWithPageStack
import one.irradia.neutrino.views.tabs.NeutrinoTabType

/**
 * The catalog tab.
 */

class NTabCatalog(
  context: Context,
  override val tabIndex: Int,
  listener: NeutrinoListenerType)
  : NTabWithPageStack(rootPage = rootPageConstructor(context), listener = listener),
  NeutrinoTabType {

  companion object {
    private fun rootPageConstructor(context: Context): NPageConstructor =
      NPageFeed.constructor(NPageFeedArguments(
        depth = 0,
        isExternalCollection = true,
        title = context.getString(R.string.neutrino_tab_catalog)))
  }
}

