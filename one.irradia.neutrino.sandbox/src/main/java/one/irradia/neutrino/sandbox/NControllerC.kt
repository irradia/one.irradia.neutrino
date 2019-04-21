package one.irradia.neutrino.sandbox

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import one.irradia.neutrino.views.NViewControllerType
import org.slf4j.LoggerFactory

class NControllerC : NViewControllerType {

  private val logger =
    LoggerFactory.getLogger(NControllerC::class.java)

  override fun onCreateView(
    context: Context,
    inflater: LayoutInflater,
    container: ViewGroup): View {
    this.logger.debug("onCreateView")
    val button = Button(context)
    button.text = "NControllerC"
    return button
  }

  override fun onAttach() {
    this.logger.debug("onAttach")
  }

  override fun onDetach() {
    this.logger.debug("onDetach")
  }

  override fun onDestroyView() {
    this.logger.debug("onDestroyView")
  }

  override fun onDestroy() {
    this.logger.debug("onDestroy")
  }
}