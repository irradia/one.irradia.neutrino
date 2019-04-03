package one.irradia.neutrino.views

import android.content.Context

/**
 * The books tab.
 */

class NBooksTab(
  context: Context,
  listener: NeutrinoListenerType)
  : NPageStackTab(rootPage = rootPageConstructor(context), listener = listener),
  NeutrinoTabType {

  companion object {
    private fun rootPageConstructor(context: Context): NPageConstructor {
      return NPageConstructor {
        NPageFeed.create(NPageFeedArguments(
          depth = 0,
          isExternalCollection = false,
          title = context.getString(R.string.neutrino_tab_books)))
      }
    }
  }
}
