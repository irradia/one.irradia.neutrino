package one.irradia.neutrino.views

import android.os.Bundle
import android.view.MenuItem

interface NeutrinoActivityAdapterType : NeutrinoListenerType {

  /**
   * @see [androidx.appcompat.app.AppCompatActivity.onBackPressed]
   */

  fun onNeutrinoBackPressed(): Boolean

  /**
   * @see [androidx.appcompat.app.AppCompatActivity.onDestroy]
   */

  fun onNeutrinoDestroy()

  /**
   * @see [androidx.appcompat.app.AppCompatActivity.onOptionsItemSelected]
   */

  fun onNeutrinoOptionsItemSelected(item: MenuItem): Boolean

  /**
   * @see [androidx.appcompat.app.AppCompatActivity.onCreate]
   */

  fun onNeutrinoCreate(savedInstanceState: Bundle?)
}