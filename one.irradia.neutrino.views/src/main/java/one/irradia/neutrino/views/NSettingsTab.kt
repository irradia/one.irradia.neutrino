package one.irradia.neutrino.views

/**
 * The settings tab.
 */

class NSettingsTab(
  listener: NeutrinoListenerType)
  : NPageStackTab(rootPage = NSettingsPageMain(), listener = listener),
  NeutrinoTabType
