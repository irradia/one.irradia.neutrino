package one.irradia.neutrino.views

import android.os.Bundle

/**
 * A convenient abstract class that takes some of the repetition out of page implementations.
 */

abstract class NPageAbstract : NeutrinoFragment(), NeutrinoPageType {

  protected lateinit var listener: NeutrinoListenerType

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)

    val context = this.requireContext()
    if (context is NeutrinoListenerType) {
      this.listener = context
    } else {
      throw ClassCastException(
        StringBuilder(64)
          .append("The context hosting this fragment must implement one or more listener interfaces.\n")
          .append("  Context: ")
          .append(context::class)
          .append('\n')
          .append("  Required interface: ")
          .append(NeutrinoListenerType::class)
          .append('\n')
          .toString())
    }
  }
}