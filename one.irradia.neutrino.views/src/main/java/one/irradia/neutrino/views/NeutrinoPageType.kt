package one.irradia.neutrino.views

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
}