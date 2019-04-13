package one.irradia.neutrino.views.api

import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.app.ActionBar.DISPLAY_HOME_AS_UP
import androidx.appcompat.app.ActionBar.DISPLAY_SHOW_HOME
import androidx.appcompat.app.AppCompatActivity
import one.irradia.neutrino.views.R
import one.irradia.neutrino.views.api.NeutrinoActivityHelper.Invalidated.INVALIDATED
import one.irradia.neutrino.views.api.NeutrinoActivityHelper.Invalidated.NOT_INVALIDATED
import one.irradia.neutrino.views.pages.NeutrinoPageType
import one.irradia.neutrino.views.tabs.NeutrinoTabType

object NeutrinoActivityHelper {

  fun onNeutrinoTabUpdated(
    activity: AppCompatActivity,
    tab: NeutrinoTabType) {

    val page = tab.tabPageCurrent()

    activity.supportFragmentManager
      .beginTransaction()
      .replace(R.id.neutrinoTabContent, page.pageFragment(), "NEUTRINO_TAB_CONTENT")
      .commit()

    updateAndRefreshActionBar(activity, tab, page)
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
    activity: AppCompatActivity,
    tab: NeutrinoTabType,
    page: NeutrinoPageType) {

    val homeInvalidated = updateHomeButton(activity, tab)
    val titleInvalidated = updateTitle(activity, page)
    if (homeInvalidated == INVALIDATED || titleInvalidated == INVALIDATED) {
      activity.supportInvalidateOptionsMenu()
    }
  }

  private fun updateTitle(
    activity: AppCompatActivity,
    page: NeutrinoPageType): Invalidated {
    val newTitle = page.pageTitle()
    if (newTitle != null) {
      if (activity.title != newTitle) {
        activity.title = newTitle
        return INVALIDATED
      }
    }
    return NOT_INVALIDATED
  }

  private fun updateHomeButton(
    activity: AppCompatActivity,
    tab: NeutrinoTabType): Invalidated {

    var invalidated = NOT_INVALIDATED
    val bar = activity.supportActionBar
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

  fun onNeutrinoTabSelected(
    activity: AppCompatActivity,
    tab: NeutrinoTabType) {
    val page = tab.tabPageCurrent()

    activity.supportFragmentManager
      .beginTransaction()
      .replace(R.id.neutrinoTabContent, page.pageFragment(), "NEUTRINO_TAB_CONTENT")
      .commit()

    updateAndRefreshActionBar(activity, tab, page)
  }

  fun onNeutrinoPageMenuUpdated(
    activity: AppCompatActivity,
    page: NeutrinoPageType) {
    updateTitle(activity, page)
  }

  fun onNeutrinoCreate(
    activity: AppCompatActivity,
    savedInstanceState: Bundle?): NeutrinoMain {

    activity.setContentView(R.layout.neutrino_activity)

    if (savedInstanceState != null) {
      activity.supportFragmentManager.executePendingTransactions()
      val fragment = findNeutrinoFragment(activity)
      if (fragment != null) {
        return fragment
      }
    }

    val fragment = NeutrinoMain()
    activity.supportFragmentManager
      .beginTransaction()
      .replace(R.id.neutrinoFragmentHolder, fragment, "NEUTRINO_TABS")
      .commit()
    return fragment
  }

  fun findNeutrinoFragment(activity: AppCompatActivity): NeutrinoMain? {
    val fragment =
      activity.supportFragmentManager.findFragmentById(R.id.neutrinoFragmentHolder)
    return if (fragment != null) {
      fragment as NeutrinoMain
    } else {
      null
    }
  }

  fun onNeutrinoBackPressed(activity: AppCompatActivity): Boolean {
    val fragment = findNeutrinoFragment(activity)
    return if (fragment != null) {
      fragment.onPressedBack()
    } else {
      true
    }
  }

  fun onNeutrinoOptionsItemSelected(activity: AppCompatActivity, item: MenuItem): Boolean {
    val fragment = findNeutrinoFragment(activity)
    return if (fragment != null) {
      fragment.onOptionsItemSelected(item)
    } else {
      true
    }
  }
}