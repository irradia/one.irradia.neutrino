package one.irradia.neutrino.views

import android.content.Context

/**
 * A convenient abstract class that takes some of the repetition out of page implementations.
 */

abstract class NPageAbstract : NeutrinoFragment(), NeutrinoPageType {

  protected lateinit var listener: NeutrinoListenerType

  final override fun onAttach(context: Context) {
    super.onAttach(context)

    if (context is NeutrinoListenerType) {
      this.listener = context
    } else {
      throw ClassCastException(
        StringBuilder(64)
          .append("The activity hosting this fragment must implement one or more listener interfaces.\n")
          .append("  Activity: ")
          .append(context::class)
          .append('\n')
          .append("  Required interface: ")
          .append(NeutrinoListenerType::class)
          .append('\n')
          .toString())
    }
  }
}