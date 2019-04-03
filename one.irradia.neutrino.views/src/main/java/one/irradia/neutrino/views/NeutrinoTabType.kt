package one.irradia.neutrino.views

/**
 * The interface exposed by tabs.
 */

interface NeutrinoTabType {

  /**
   * Retrieve the current page within the tab.
   */

  fun tabPageCurrent(): NeutrinoPageType

  /**
   * `true` if an "up" button should be displayed in the menu for the tab
   */

  fun tabWantsUpButton(): Boolean

  /**
   * The user pressed the "up" button. The tab implementation should perform any required
   * actions such as popping pages from internal stacks, etc.
   */

  fun onPressedUp(): Boolean

  /**
   * The user pressed the "back" button. The tab implementation should perform any required
   * actions such as popping pages from internal stacks, etc.
   */

  fun onPressedBack(): Boolean
}