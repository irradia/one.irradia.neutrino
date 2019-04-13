package one.irradia.neutrino.views

import android.content.Context
import one.irradia.neutrino.views.api.NeutrinoListenerType
import one.irradia.neutrino.views.tabs.NTabWithPageStack
import one.irradia.neutrino.views.tabs.NeutrinoTabType

/**
 * The settings tab.
 */

class NTabSettings(
  context: Context,
  override val tabIndex: Int,
  listener: NeutrinoListenerType)
  : NTabWithPageStack(rootPage = NSettingsPageMain.constructor(), listener = listener),
  NeutrinoTabType
