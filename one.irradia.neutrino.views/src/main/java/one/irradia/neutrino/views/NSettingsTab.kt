package one.irradia.neutrino.views

import android.content.Context

/**
 * The settings tab.
 */

class NSettingsTab(
  context: Context,
  listener: NeutrinoListenerType)
  : NPageStackTab(rootPage = NSettingsPageMain(), listener = listener),
  NeutrinoTabType
