package one.irradia.neutrino.views

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.android.material.tabs.TabLayout
import com.google.common.base.Preconditions
import org.slf4j.LoggerFactory

class NTabController(
  private val controllers: List<NTabControllerItem>) : NViewControllerType {

  init {
    Preconditions.checkArgument(
      this.controllers.isNotEmpty(),
      "Must provide a non-empty list of controllers")
  }

  private val logger =
    LoggerFactory.getLogger(NTabController::class.java)

  override fun onAttach() {
    this.logger.debug("onAttach")
    this.controllers.forEach { controller -> controller.controller.onAttach() }
  }

  override fun onDetach() {
    this.logger.debug("onDetach")
    this.controllers.forEach { controller -> controller.controller.onDetach() }
  }

  override fun onDestroy() {
    this.logger.debug("onDestroy")
    this.controllers.forEach { controller -> controller.controller.onDestroy() }
  }

  override fun onDestroyView() {
    this.logger.debug("onDestroyView")
  }

  private var tabSelected = 0
  private lateinit var tabLayout: TabLayout
  private lateinit var tabContent: ViewGroup
  private lateinit var tabViewSelected: View

  override fun onCreateView(
    context: Context,
    inflater: LayoutInflater,
    container: ViewGroup): View {

    this.logger.debug("onCreateView")

    val rootView =
      inflater.inflate(R.layout.neutrino_tabs, container, false)
    this.tabContent =
      rootView.findViewById(R.id.neutrinoTabContent)
    this.tabLayout =
      rootView.findViewById(R.id.neutrinoTabLayout)

    for (controller in this.controllers) {
      val tab = this.tabLayout.newTab()
      tab.text = controller.title
      tab.icon = controller.icon
      this.tabLayout.addTab(tab)
    }

    this.tabLayout.addOnTabSelectedListener(
      object : TabLayout.BaseOnTabSelectedListener<TabLayout.Tab> {
        override fun onTabReselected(tab: TabLayout.Tab) {
          this@NTabController.selectTab(context, inflater, tab.position)
        }

        override fun onTabUnselected(tab: TabLayout.Tab) {

        }

        override fun onTabSelected(tab: TabLayout.Tab) {
          this@NTabController.selectTab(context, inflater, tab.position)
        }
      })

    this.tabViewSelected =
      this.controllers[0].controller.onCreateView(context, inflater, this.tabContent)
    this.tabContent.addView(this.tabViewSelected)

    this.selectTab(context, inflater, 0)
    return rootView
  }

  private fun selectTab(
    context: Context,
    inflater: LayoutInflater,
    position: Int) {
    if (this.tabSelected != position) {
      val oldController =
        this.controllers[this.tabSelected].controller
      val newController =
        this.controllers[position].controller
      val newView =
        newController.onCreateView(context, inflater, this.tabContent)

      this.tabContent.removeView(this.tabViewSelected)
      oldController.onDestroyView()

      this.tabContent.addView(newView)
      this.tabViewSelected = newView
      this.tabSelected = position
    }
  }
}