package one.irradia.neutrino.views

/**
 * An abstract implementation of a tab that contains a non-empty stack of pages.
 *
 * Initially, the stack contains one page. Implementations of this abstract class are responsible
 * for pushing pages onto the stack when necessary, and popping pages from the stack if
 * necessary.
 */

abstract class NPageStackTab(
  rootPage: NeutrinoPageType,
  private val listener: NeutrinoListenerType) : NeutrinoTabType {

  private val pageStack = mutableListOf(rootPage)

  final override fun tabPageCurrent(): NeutrinoPageType =
    this.pageStack.last()

  final override fun onPressedUp(): Boolean =
    this.pagePop()

  final override fun tabWantsUpButton(): Boolean =
    this.pageStack.size > 1

  final override fun onPressedBack(): Boolean =
    true

  protected fun pagePop(): Boolean =
    if (this.pageStack.size > 1) {
      this.pageStack.removeAt(this.pageStack.lastIndex)
      this.listener.onNeutrinoTabSelected(this)
      true
    } else {
      false
    }

  protected fun pagePush(page: NeutrinoPageType) {
    this.pageStack.add(page)
    this.listener.onNeutrinoTabPageStackChanged(this)
  }
}
