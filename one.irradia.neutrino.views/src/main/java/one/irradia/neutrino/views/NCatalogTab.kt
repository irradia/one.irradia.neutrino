package one.irradia.neutrino.views

import android.content.Context

/**
 * The catalog tab.
 */

class NCatalogTab(
  context: Context,
  override val tabIndex: Int,
  listener: NeutrinoListenerType)
  : NPageStackTab(rootPage = rootPageConstructor(context), listener = listener),
  NeutrinoTabType {

  companion object {
    private fun rootPageConstructor(context: Context): NPageConstructor =
      NPageFeed.constructor(NPageFeedArguments(
        depth = 0,
        isExternalCollection = true,
        title = context.getString(R.string.neutrino_tab_catalog)))
  }
}

