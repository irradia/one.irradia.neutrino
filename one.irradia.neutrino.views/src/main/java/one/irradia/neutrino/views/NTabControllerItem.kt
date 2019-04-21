package one.irradia.neutrino.views

import android.graphics.drawable.Drawable

data class NTabControllerItem(
  val title: String,
  val icon: Drawable,
  val controller: NViewControllerType)
