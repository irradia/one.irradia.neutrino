package one.irradia.neutrino.views

import android.os.Bundle
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import com.google.android.material.tabs.TabLayout
import io.reactivex.subjects.PublishSubject
import java.io.Serializable

class NeutrinoMain : NeutrinoFragment() {

  companion object {
    private const val SAVED_STATE_KEY = "one.irradia.neutrino.views.NeutrinoMain.state"
  }

  val eventBus: PublishSubject<NeutrinoEventType> = PublishSubject.create<NeutrinoEventType>()

  private lateinit var tabLayout: TabLayout
  private lateinit var tabCatalog: NCatalogTab
  private lateinit var tabBooks: NBooksTab
  private lateinit var tabReservations: NReservationsTab
  private lateinit var tabSettings: NSettingsTab
  private lateinit var listener: NeutrinoListenerType
  private lateinit var tabListener: NTabListener
  private lateinit var tabCurrent: NeutrinoTabType
  private lateinit var tabsByIndex: Map<Int, NeutrinoTabType>
  private var tabCurrentIndex: Int = 0
  private var tabStates: Map<Int, Serializable>? = null

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)

    /*
     * Restore the saved state if necessary.
     */

    if (savedInstanceState != null) {
      val savedState =
        savedInstanceState.getSerializable(SAVED_STATE_KEY) as SavedState?

      if (savedState != null) {
        this.tabCurrentIndex = savedState.selectedTabIndex
        this.tabStates = savedState.tabStates
      }
    }

    val context = this.requireContext()
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

  data class SavedState(
    val selectedTabIndex: Int,
    val tabStates: Map<Int, Serializable>)
    : Serializable

  override fun onSaveInstanceState(outState: Bundle) {
    super.onSaveInstanceState(outState)

    val tabStates = HashMap<Int, Serializable>()
    for (entry in this.tabsByIndex) {
      tabStates[entry.key] = entry.value.tabSaveState()
    }

    val state =
      SavedState(
        selectedTabIndex = this.tabCurrentIndex,
        tabStates = tabStates)

    outState.putSerializable(SAVED_STATE_KEY, state)
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
    return view
  }

  override fun onStart() {
    super.onStart()

    val context = this.requireContext()

    this.tabCatalog =
      NCatalogTab(context, this.listener)
    this.tabBooks =
      NBooksTab(context, this.listener)
    this.tabReservations =
      NReservationsTab(context, this.listener)
    this.tabSettings =
      NSettingsTab(context, this.listener)

    this.tabsByIndex =
      mapOf(
        Pair(0, this.tabCatalog),
        Pair(1, this.tabBooks),
        Pair(2, this.tabReservations),
        Pair(3, this.tabSettings))

    val savedStates = this.tabStates
    if (savedStates != null) {
      for (entry in this.tabsByIndex) {
        val state = savedStates[entry.key]
        if (state != null) {
          entry.value.tabRestoreState(state)
        }
      }
      this.tabStates = null
    }

    this.tabLayout.getTabAt(this.tabCurrentIndex)?.select()
  }

  override fun onOptionsItemSelected(item: MenuItem): Boolean {
    return when (item.itemId) {
      android.R.id.home -> this.onPressedUp()
      else -> true
    }
  }

  inner class NTabListener : TabLayout.OnTabSelectedListener {
    override fun onTabReselected(tab: TabLayout.Tab) {
      val index = tab.position
      val neutrinoTab = this@NeutrinoMain.tabsByIndex[index]
      if (neutrinoTab != null) {
        this@NeutrinoMain.selectTab(neutrinoTab, index)
      }
    }

    override fun onTabUnselected(tab: TabLayout.Tab) {

    }

    override fun onTabSelected(tab: TabLayout.Tab) {
      val index = tab.position
      val neutrinoTab = this@NeutrinoMain.tabsByIndex[index]
      if (neutrinoTab != null) {
        this@NeutrinoMain.selectTab(neutrinoTab, index)
      }
    }
  }

  private fun selectTab(tab: NeutrinoTabType, index: Int) {
    this.tabCurrentIndex = index
    this.tabCurrent = tab
    this.listener.onNeutrinoTabSelected(tab)
  }

  fun onPressedBack(): Boolean {
    return this.tabCurrent.onPressedBack()
  }

  fun onPressedUp(): Boolean {
    return this.tabCurrent.onPressedUp()
  }
}