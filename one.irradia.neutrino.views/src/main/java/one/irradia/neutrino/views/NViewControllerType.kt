package one.irradia.neutrino.views

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

interface NViewControllerType {

  fun onCreateView(
    context: Context,
    inflater: LayoutInflater,
    container: ViewGroup): View

  fun onAttach()

  fun onDetach()

  fun onDestroyView()

  fun onDestroy()

}