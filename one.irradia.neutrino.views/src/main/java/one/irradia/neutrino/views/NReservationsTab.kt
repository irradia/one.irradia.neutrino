package one.irradia.neutrino.views

import android.content.Context

/**
 * The reservations tab.
 */

class NReservationsTab(
  context: Context,
  override val tabIndex: Int,
  listener: NeutrinoListenerType)
  : NPageStackTab(rootPage = rootPageConstructor(context), listener = listener),
  NeutrinoTabType {

  companion object {
    private fun rootPageConstructor(context: Context): NPageConstructor =
      NPageFeed.constructor(NPageFeedArguments(
        depth = 0,
        isExternalCollection = false,
        title = context.getString(R.string.neutrino_tab_reservations)))
  }
}
