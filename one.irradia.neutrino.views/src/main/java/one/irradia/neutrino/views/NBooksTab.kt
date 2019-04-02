package one.irradia.neutrino.views

import android.content.Context

/**
 * The books tab.
 */

class NBooksTab(
  context: Context,
  listener: NeutrinoListenerType)
  : NPageStackTab(rootPage = NPageFeed.create(startingArguments(context)), listener = listener),
  NeutrinoTabType {

  companion object {
    private fun startingArguments(context: Context): NPageFeedArguments {
      return NPageFeedArguments(
        depth = 0,
        isExternalCollection = false,
        title = context.getString(R.string.neutrino_tab_books))
    }
  }
}
