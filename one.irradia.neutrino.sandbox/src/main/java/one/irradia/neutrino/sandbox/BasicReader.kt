package one.irradia.neutrino.sandbox

import android.os.Bundle
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import one.irradia.neutrino.views.NTabController
import one.irradia.neutrino.views.NTabControllerItem
import org.slf4j.LoggerFactory

class BasicReader : AppCompatActivity() {

  private val logger = LoggerFactory.getLogger(BasicReader::class.java)

  private lateinit var content: ViewGroup
  private lateinit var toolbar: Toolbar

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)

    this.logger.debug("onCreate: {}", savedInstanceState)

    this.setContentView(R.layout.neutrino_activity)

    this.content =
      this.findViewById(R.id.neutrinoContent)
    this.toolbar =
      this.findViewById(R.id.neutrinoToolbar)

    this.toolbar.title = "Neutrino Toolbar"
    this.toolbar.inflateMenu(R.menu.neutrino_page_feed_external_menu)

    val controller =
      NTabController(
        controllers = listOf(
          NTabControllerItem(
            title = "Controller A",
            icon = this.resources.getDrawable(R.drawable.neutrino_icon_books, this.theme),
            controller = NControllerA()
          ),
          NTabControllerItem(
            title = "Controller B",
            icon = this.resources.getDrawable(R.drawable.neutrino_icon_books, this.theme),
            controller = NControllerB()
          ),

          NTabControllerItem(
            title = "Controller B",
            icon = this.resources.getDrawable(R.drawable.neutrino_icon_books, this.theme),
            controller = NTabController(
              controllers = listOf(
                NTabControllerItem(
                  title = "Controller A",
                  icon = this.resources.getDrawable(R.drawable.neutrino_icon_books, this.theme),
                  controller = NControllerA()
                ),
                NTabControllerItem(
                  title = "Controller B",
                  icon = this.resources.getDrawable(R.drawable.neutrino_icon_books, this.theme),
                  controller = NControllerB()
                )
              ))
          ),

          NTabControllerItem(
            title = "Controller C",
            icon = this.resources.getDrawable(R.drawable.neutrino_icon_books, this.theme),
            controller = NControllerC())))

    val controllerView =
      controller.onCreateView(this, this.layoutInflater, this.content)

    this.content.addView(controllerView)
    controller.onAttach()
  }
}