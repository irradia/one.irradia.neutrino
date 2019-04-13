package one.irradia.neutrino.views.tabs

import one.irradia.neutrino.views.pages.NPageConstructor
import one.irradia.neutrino.views.pages.NeutrinoPageType
import java.io.Serializable

/**
 * The interface exposed by tabs.
 */

interface NeutrinoTabType {

  /**
   * The index of the tab
   */

  val tabIndex: Int

  /**
   * Create a page on the tab.
   */

  fun tabPageCreate(constructor: NPageConstructor)

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

  /**
   * Return `true` if the tab has the given page anywhere in the stack
   */

  fun tabHasPage(page: NeutrinoPageType): Boolean
}