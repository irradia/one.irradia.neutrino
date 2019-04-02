package one.irradia.neutrino.views

import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.app.ActionBar.DISPLAY_HOME_AS_UP
import androidx.appcompat.app.ActionBar.DISPLAY_SHOW_HOME
import androidx.appcompat.app.AppCompatActivity
import io.reactivex.subjects.PublishSubject
import io.reactivex.subjects.Subject
import one.irradia.neutrino.views.NeutrinoActivityAdapter.Invalidated.INVALIDATED
import one.irradia.neutrino.views.NeutrinoActivityAdapter.Invalidated.NOT_INVALIDATED

/**
 * An adapter that performs much of the work required to have an activity correctly
 * communicate with the main fragment.
 */

class NeutrinoActivityAdapter(
  private val activity: AppCompatActivity): NeutrinoActivityAdapterType {

  private lateinit var eventBusVar: Subject<NeutrinoEventType>
  private lateinit var neutrinoMain: NeutrinoMain

  override val neutrinoEventBus: Subject<NeutrinoEventType>
    get() = this.eventBusVar

  override fun onNeutrinoTabPageStackChanged(tab: NeutrinoTabType) {
    val page = tab.tabPageCurrent()

    this.activity.supportFragmentManager
      .beginTransaction()
      .replace(R.id.neutrinoTabContent, page.pageFragment(), "NEUTRINO_TAB_CONTENT")
      .commit()

    this.updateAndRefreshActionBar(tab, page)
  }

  enum class Invalidated {
    INVALIDATED,
    NOT_INVALIDATED
  }

  /**
   * Carefully update the action bar, taking pains to avoid updating it unless absolutely necessary.
   * The reason for this excessive care is that menu invalidation tends to be rather expensive and
   * can lead to sluggish menu behaviour on some devices if lots of redundant invalidations are
   * being performed.
   */

  private fun updateAndRefreshActionBar(
    tab: NeutrinoTabType,
    page: NeutrinoPageType) {

    val homeInvalidated = updateHomeButton(tab)
    val titleInvalidated = updateTitle(page)
    if (homeInvalidated == INVALIDATED || titleInvalidated == INVALIDATED) {
      this.activity.supportInvalidateOptionsMenu()
    }
  }

  private fun updateTitle(page: NeutrinoPageType): Invalidated {
    val newTitle = page.pageTitle()
    if (newTitle != null) {
      if (this.activity.title != newTitle) {
        this.activity.title = newTitle
        return INVALIDATED
      }
    }
    return NOT_INVALIDATED
  }

  private fun updateHomeButton(tab: NeutrinoTabType): Invalidated {
    var invalidated = NOT_INVALIDATED
    val bar = this.activity.supportActionBar
    if (bar != null) {
      if (tab.tabWantsUpButton()) {
        if (bar.displayOptions.and(DISPLAY_SHOW_HOME) == 0) {
          bar.setHomeButtonEnabled(true)
          invalidated = INVALIDATED
        }
        if (bar.displayOptions.and(DISPLAY_HOME_AS_UP) == 0) {
          bar.setDisplayHomeAsUpEnabled(true)
          invalidated = INVALIDATED
        }
      } else {
        if (bar.displayOptions.and(DISPLAY_SHOW_HOME) == DISPLAY_SHOW_HOME) {
          bar.setHomeButtonEnabled(false)
          invalidated = INVALIDATED
        }
        if (bar.displayOptions.and(DISPLAY_HOME_AS_UP) == DISPLAY_HOME_AS_UP) {
          bar.setDisplayHomeAsUpEnabled(false)
          invalidated = INVALIDATED
        }
      }
    }
    return invalidated
  }

  override fun onNeutrinoTabSelected(tab: NeutrinoTabType) {
    val page = tab.tabPageCurrent()

    this.activity.supportFragmentManager
      .beginTransaction()
      .replace(R.id.neutrinoTabContent, page.pageFragment(), "NEUTRINO_TAB_CONTENT")
      .commit()

    this.updateAndRefreshActionBar(tab, page)
  }

  override fun onNeutrinoPageMenuUpdated(page: NeutrinoPageType) {
    this.updateTitle(page)
  }

  override fun onNeutrinoBackPressed(): Boolean {
    return this.neutrinoMain.onBackPressed()
  }

  override fun onNeutrinoDestroy() {
    this.neutrinoEventBus.onComplete()
  }

  override fun onNeutrinoOptionsItemSelected(item: MenuItem): Boolean {
    when (item.itemId) {
      android.R.id.home ->
        if (this.neutrinoMain.onUpPressed()) {
          return true
        }
    }

    return false
  }

  override fun onNeutrinoCreate(savedInstanceState: Bundle?) {
    this.eventBusVar = PublishSubject.create<NeutrinoEventType>()
    this.activity.setContentView(R.layout.neutrino_activity)

    this.neutrinoMain = NeutrinoMain()
    this.activity.supportFragmentManager
      .beginTransaction()
      .replace(R.id.neutrinoFragmentHolder, this.neutrinoMain, "NEUTRINO_TABS")
      .commit()
  }
}
