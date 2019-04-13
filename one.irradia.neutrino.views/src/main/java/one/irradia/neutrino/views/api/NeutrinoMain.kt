package one.irradia.neutrino.views.api

import android.os.Bundle
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import com.google.android.material.tabs.TabLayout
import io.reactivex.disposables.Disposable
import io.reactivex.subjects.PublishSubject
import one.irradia.neutrino.views.NTabBooks
import one.irradia.neutrino.views.NTabCatalog
import one.irradia.neutrino.views.NTabReservations
import one.irradia.neutrino.views.NTabSettings
import one.irradia.neutrino.views.R
import one.irradia.neutrino.views.pages.NPageConstructor
import one.irradia.neutrino.views.pages.NeutrinoPageEvent
import one.irradia.neutrino.views.tabs.NeutrinoTabEvent
import one.irradia.neutrino.views.tabs.NeutrinoTabType
import java.io.Serializable

class NeutrinoMain : NeutrinoFragment() {

  companion object {
    private const val SAVED_STATE_KEY = "one.irradia.neutrino.views.api.NeutrinoMain.state"
  }

  val eventBus: PublishSubject<NeutrinoEventType> = PublishSubject.create<NeutrinoEventType>()

  private lateinit var tabLayout: TabLayout
  private lateinit var tabCatalog: NTabCatalog
  private lateinit var tabBooks: NTabBooks
  private lateinit var tabReservations: NTabReservations
  private lateinit var tabSettings: NTabSettings
  private lateinit var listener: NeutrinoListenerType
  private lateinit var tabListener: NTabListener
  private lateinit var tabCurrent: NeutrinoTabType
  private lateinit var tabsByIndex: Map<Int, NeutrinoTabType>
  private var tabCurrentIndex: Int = 0
  private var tabStates: Map<Int, Serializable>? = null
  private var pageEventSubscription: Disposable? = null
  private var tabEventSubscription: Disposable? = null

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
      NTabCatalog(context, 0, this.listener)
    this.tabBooks =
      NTabBooks(context, 1, this.listener)
    this.tabReservations =
      NTabReservations(context, 2, this.listener)
    this.tabSettings =
      NTabSettings(context, 3, this.listener)

    this.tabsByIndex =
      listOf(this.tabCatalog, this.tabBooks, this.tabReservations, this.tabSettings)
        .map { tab -> Pair(tab.tabIndex, tab) }
        .toMap()

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

    this.pageEventSubscription =
      this.eventBus.ofType(NeutrinoPageEvent::class.java)
        .subscribe { event -> this.onPageEvent(event) }

    this.tabEventSubscription =
      this.eventBus.ofType(NeutrinoTabEvent::class.java)
        .subscribe { event -> this.onTabEvent(event) }
  }

  override fun onStop() {
    super.onStop()
    this.pageEventSubscription?.dispose()
    this.tabEventSubscription?.dispose()
  }

  private fun onTabEvent(event: NeutrinoTabEvent) {
    when (event) {
      is NeutrinoTabEvent.TabPageStackChanged -> {
        if (event.tab.tabIndex == this.tabCurrentIndex) {
          this.listener.onNeutrinoTabUpdated(event.tab)
        }
      }
    }
  }

  private fun onPageEvent(event: NeutrinoPageEvent) {
    when (event) {
      is NeutrinoPageEvent.OpenPageOnCurrentTab ->
        for (entry in tabsByIndex) {
          if (entry.value.tabHasPage(event.callingPage)) {
            openPageOnTab(entry.key, entry.value, event.constructor)
            return
          }
        }

      is NeutrinoPageEvent.OpenPageOnSpecificTab ->
        for (entry in tabsByIndex) {
          if (entry.value.javaClass == event.tab) {
            openPageOnTab(entry.key, entry.value, event.constructor)
            return
          }
        }
    }
  }

  private fun openPageOnTab(
    tabIndex: Int,
    tab: NeutrinoTabType,
    constructor: NPageConstructor) {

    if (this.tabCurrentIndex != tab.tabIndex) {
      this.tabLayout.getTabAt(tabIndex)?.select()
    }

    tab.tabPageCreate(constructor)
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