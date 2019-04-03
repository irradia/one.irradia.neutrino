package one.irradia.neutrino.views

import java.io.Serializable

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
   * Save the state of the tab for later restoration.
   */

  fun tabSaveState(): Serializable

  /**
   * Restore the tab from the given state.
   */

  fun tabRestoreState(state: Serializable)

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