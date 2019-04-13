package one.irradia.neutrino.views.tabs

import one.irradia.neutrino.views.api.NeutrinoListenerType
import one.irradia.neutrino.views.tabs.NeutrinoTabEvent.TabPageStackChanged
import one.irradia.neutrino.views.pages.NPageConstructor
import one.irradia.neutrino.views.pages.NeutrinoPageType
import java.io.Serializable

/**
 * An abstract implementation of a tab that contains a non-empty stack of pages.
 *
 * Initially, the stack contains one page. Implementations of this abstract class are responsible
 * for pushing pages onto the stack when necessary, and popping pages from the stack if
 * necessary.
 */

abstract class NTabWithPageStack(
  private val rootPage: NPageConstructor,
  private val listener: NeutrinoListenerType) : NeutrinoTabType {

  private val pageStack: MutableList<NeutrinoPageType> =
    mutableListOf(this.rootPage.constructor.invoke())

  final override fun tabSaveState(): Serializable {
    val stack = ArrayList<NPageConstructor>()
    for (page in this.pageStack) {
      val constructor = page.pageSaveState()
      if (constructor != null) {
        stack.add(constructor)
      }
    }
    return stack
  }

  final override fun tabRestoreState(state: Serializable) {
    val stack: List<NPageConstructor> = state as List<NPageConstructor>
    if (!stack.isEmpty()) {
      this.pageStack.clear()
      for (pageConstructor in stack) {
        this.pageStack.add(pageConstructor.constructor.invoke())
      }
    } else {
      this.pageStack.clear()
      this.pageStack.add(this.rootPage.constructor.invoke())
    }
  }

  final override fun tabPageCurrent(): NeutrinoPageType =
    this.pageStack.last()

  final override fun onPressedUp(): Boolean =
    this.pagePop()

  final override fun tabWantsUpButton(): Boolean =
    this.pageStack.size > 1

  final override fun onPressedBack(): Boolean =
    true

  final override fun tabPageCreate(constructor: NPageConstructor) =
    this.pagePush(constructor.constructor.invoke())

  final override fun tabHasPage(page: NeutrinoPageType): Boolean =
    this.pageStack.contains(page)

  protected fun pagePop(): Boolean =
    if (this.pageStack.size > 1) {
      this.pageStack.removeAt(this.pageStack.lastIndex)
      this.listener.neutrinoEventBus.onNext(TabPageStackChanged(this))
      true
    } else {
      false
    }

  protected fun pagePush(page: NeutrinoPageType) {
    this.pageStack.add(page)
    this.listener.neutrinoEventBus.onNext(TabPageStackChanged(this))
  }
}
