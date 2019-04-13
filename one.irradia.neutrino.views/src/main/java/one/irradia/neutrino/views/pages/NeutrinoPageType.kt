package one.irradia.neutrino.views.pages

import androidx.fragment.app.Fragment

/**
 * The type of pages.
 */

interface NeutrinoPageType {

  /**
   * The current fragment for the page.
   */

  fun pageFragment(): Fragment

  /**
   * The current title for the page, if any.
   */

  fun pageTitle(): String?

  /**
   * Save a function that can recreate the current page when evaluated.
   */

  fun pageSaveState(): NPageConstructor?
}