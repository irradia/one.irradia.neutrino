package one.irradia.neutrino.feeds.spi

import one.irradia.mime.api.MIMEType
import java.net.URI

data class NSPIFeedDescription(
  val uri: URI,
  val mimeType: MIMEType)