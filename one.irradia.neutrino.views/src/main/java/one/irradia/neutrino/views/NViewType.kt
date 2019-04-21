package one.irradia.neutrino.views

import android.os.Bundle
import android.view.View

interface NViewType {

  fun rootView(): View

  fun viewState(): Bundle

}
