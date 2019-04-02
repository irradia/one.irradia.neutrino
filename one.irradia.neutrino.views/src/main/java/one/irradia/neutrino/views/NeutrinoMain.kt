package one.irradia.neutrino.views

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.google.android.material.tabs.TabLayout

class NeutrinoMain : Fragment() {

  private lateinit var tabLayout: TabLayout
  private lateinit var tabCatalog: NCatalogTab
  private lateinit var tabBooks: NBooksTab
  private lateinit var tabReservations: NReservationsTab
  private lateinit var tabSettings: NSettingsTab
  private lateinit var listener: NeutrinoListenerType
  private lateinit var tabListener: NTabListener
  private lateinit var tabCurrent: NeutrinoTabType
  private lateinit var tabs: List<NeutrinoTabType>

  override fun onAttach(context: Context) {
    super.onAttach(context)

    if (context is NeutrinoListenerType) {
      this.listener = context

      this.tabCatalog = NCatalogTab(context, this.listener)
      this.tabBooks = NBooksTab(context, this.listener)
      this.tabReservations = NReservationsTab(context, this.listener)
      this.tabSettings = NSettingsTab(this.listener)

      this.tabs = listOf(this.tabCatalog, this.tabBooks, this.tabReservations, this.tabSettings)
      this.tabCurrent = this.tabCatalog
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

  override fun onCreateView(
    inflater: LayoutInflater,
    container: ViewGroup?,
    savedInstanceState: Bundle?): View? {
    super.onCreateView(inflater, container, savedInstanceState)

    val view = inflater.inflate(R.layout.neutrino_tabs, container, false)
    this.tabLayout = view.findViewById(R.id.neutrinoTabLayout)
    this.tabListener = this.NTabListener()
    this.tabLayout.addOnTabSelectedListener(this.tabListener)
    this.selectCatalog()
    return view
  }

  inner class NTabListener : TabLayout.OnTabSelectedListener {
    override fun onTabReselected(tab: TabLayout.Tab) {

    }

    override fun onTabUnselected(tab: TabLayout.Tab) {

    }

    override fun onTabSelected(tab: TabLayout.Tab) {
      when (tab.position) {
        0 -> this@NeutrinoMain.selectCatalog()
        1 -> this@NeutrinoMain.selectBooks()
        2 -> this@NeutrinoMain.selectReservations()
        3 -> this@NeutrinoMain.selectSettings()
      }
    }
  }

  private fun selectTab(tab: NeutrinoTabType) {
    this.tabCurrent = tab
    this.listener.onNeutrinoTabSelected(tab)
  }

  private fun selectSettings() =
    this.selectTab(this.tabSettings)

  private fun selectReservations() =
    this.selectTab(this.tabReservations)

  private fun selectBooks() =
    this.selectTab(this.tabBooks)

  private fun selectCatalog() =
    this.selectTab(this.tabCatalog)

  fun onBackPressed(): Boolean {
    return false
  }

  fun onUpPressed(): Boolean {
    return this.tabCurrent.onUpPressed()
  }
}