package one.irradia.neutrino.views

import android.content.Context

/**
 * The settings tab.
 */

class NSettingsTab(
  context: Context,
  listener: NeutrinoListenerType)
  : NPageStackTab(rootPage = rootPageConstructor(), listener = listener),
  NeutrinoTabType {

  companion object {
    private fun rootPageConstructor(): NPageConstructor {
      return NPageConstructor {
        NSettingsPageMain.create(NSettingsPageMain.Parameters(unused = 23))
      }
    }
  }
}
