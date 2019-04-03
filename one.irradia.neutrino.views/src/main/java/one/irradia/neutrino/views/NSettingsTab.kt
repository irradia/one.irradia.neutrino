package one.irradia.neutrino.views

import android.content.Context

/**
 * The settings tab.
 */

class NSettingsTab(
  context: Context,
  override val tabIndex: Int,
  listener: NeutrinoListenerType)
  : NPageStackTab(rootPage = NSettingsPageMain.constructor(), listener = listener),
  NeutrinoTabType
