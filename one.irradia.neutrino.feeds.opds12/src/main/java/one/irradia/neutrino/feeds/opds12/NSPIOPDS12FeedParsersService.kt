package one.irradia.neutrino.feeds.opds12

import one.irradia.neutrino.feeds.spi.NSPIFeedDescription
import one.irradia.neutrino.feeds.spi.NSPIFeedEntryParserType
import one.irradia.neutrino.feeds.spi.NSPIFeedParserProviderType
import one.irradia.neutrino.feeds.spi.NSPIFeedParserType
import one.irradia.neutrino.feeds.spi.NSPIVersion
import one.irradia.opds1_2.dublin.OPDS12DublinFeedEntryParsers
import one.irradia.opds1_2.dublin.OPDS12DublinFeedParsers
import one.irradia.opds1_2.nypl.OPDS12NYPLFeedEntryParsers
import one.irradia.opds1_2.parser.api.OPDS12FeedEntryParserProviderType
import one.irradia.opds1_2.parser.api.OPDS12FeedParserProviderType
import one.irradia.opds1_2.parser.extension.spi.OPDS12FeedEntryExtensionParserProviderType
import one.irradia.opds1_2.parser.extension.spi.OPDS12FeedExtensionParserProviderType
import org.slf4j.LoggerFactory
import java.io.InputStream
import java.util.ServiceLoader

/**
 * A feed parser provider for OPDS 1.2 feeds.
 */

class NSPIOPDS12FeedParsersService : NSPIFeedParserProviderType {

  private val logger =
    LoggerFactory.getLogger(NSPIOPDS12FeedParsersService::class.java)

  private fun <T : Any> loadService(clazz: Class<T>): T {
    val loader = ServiceLoader.load(clazz)
    loader.reload()
    val services = loader.toList()
    if (services.isEmpty()) {
      throw IllegalStateException("No available services of type ${clazz.canonicalName}")
    }

    val first = services.first()!!
    this.logger.debug("loaded service: {}", first::class.java.canonicalName)
    return first
  }

  private val extensionParsers: List<OPDS12FeedExtensionParserProviderType> =
    listOf(OPDS12DublinFeedParsers())

  private val extensionEntryParsers: List<OPDS12FeedEntryExtensionParserProviderType> =
    listOf(
      OPDS12NYPLFeedEntryParsers(),
      OPDS12DublinFeedEntryParsers())

  private val opdsParsers =
    this.loadService(OPDS12FeedParserProviderType::class.java)

  private val opdsEntryParsers =
    this.loadService(OPDS12FeedEntryParserProviderType::class.java)

  private val parsers =
    NSPIOPDS12FeedParsers(
      opdsParsers = opdsParsers,
      opdsFeedEntryParsers = opdsEntryParsers,
      extensionEntryParsers = this.extensionEntryParsers,
      extensionParsers = this.extensionParsers)

  override val name: String =
    this.parsers.name

  override val version: NSPIVersion =
    this.parsers.version

  override fun canSupportFeed(description: NSPIFeedDescription): Boolean =
    this.parsers.canSupportFeed(description)

  override fun createFeedParserFor(description: NSPIFeedDescription, inputStream: InputStream): NSPIFeedParserType =
    this.parsers.createFeedParserFor(description, inputStream)

  override fun canSupportFeedEntry(description: NSPIFeedDescription): Boolean =
    this.parsers.canSupportFeedEntry(description)

  override fun createFeedEntryParserFor(description: NSPIFeedDescription, inputStream: InputStream): NSPIFeedEntryParserType =
    this.parsers.createFeedEntryParserFor(description, inputStream)

}
