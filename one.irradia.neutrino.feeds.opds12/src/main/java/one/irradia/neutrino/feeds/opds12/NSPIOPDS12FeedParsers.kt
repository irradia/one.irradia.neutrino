package one.irradia.neutrino.feeds.opds12

import one.irradia.neutrino.feeds.spi.NSPIFeedDescription
import one.irradia.neutrino.feeds.spi.NSPIFeedEntryParserType
import one.irradia.neutrino.feeds.spi.NSPIFeedParserProviderType
import one.irradia.neutrino.feeds.spi.NSPIFeedParserType
import one.irradia.neutrino.feeds.spi.NSPIVersion
import one.irradia.opds1_2.api.OPDS12FeedParseConfiguration
import one.irradia.opds1_2.parser.api.OPDS12FeedEntryParserProviderType
import one.irradia.opds1_2.parser.api.OPDS12FeedParseRequest
import one.irradia.opds1_2.parser.api.OPDS12FeedParseTarget
import one.irradia.opds1_2.parser.api.OPDS12FeedParserProviderType
import one.irradia.opds1_2.parser.extension.spi.OPDS12FeedEntryExtensionParserProviderType
import one.irradia.opds1_2.parser.extension.spi.OPDS12FeedExtensionParserProviderType
import org.slf4j.LoggerFactory
import java.io.InputStream
import java.lang.UnsupportedOperationException

/**
 * A feed parser provider for OPDS 1.2 feeds.
 */

class NSPIOPDS12FeedParsers(
  private val opdsParsers: OPDS12FeedParserProviderType,
  private val opdsFeedEntryParsers: OPDS12FeedEntryParserProviderType,
  private val extensionParsers: List<OPDS12FeedExtensionParserProviderType>,
  private val extensionEntryParsers: List<OPDS12FeedEntryExtensionParserProviderType>)
  : NSPIFeedParserProviderType {

  private val logger =
    LoggerFactory.getLogger(NSPIOPDS12FeedParsers::class.java)

  private var versionVar: NSPIVersion =
    NSPIVersion(0, 0, 0)

  init {
    try {
      this.versionVar = NSPIVersion.parse(BuildConfig.PARSER_PROVIDER_VERSION)
    } catch (e: Exception) {
      this.logger.debug("could not parse version: ", e)
    }
  }

  override val version: NSPIVersion =
    this.versionVar

  override val name =
    "one.irradia.neutrino.feeds.opds12"

  override fun canSupportFeed(description: NSPIFeedDescription): Boolean =
    when (description.mimeType.fullType) {
      "application/atom+xml" -> true
      else -> false
    }

  override fun canSupportFeedEntry(description: NSPIFeedDescription): Boolean =
    when (description.mimeType.fullType) {
      "application/atom+xml" -> true
      else -> false
    }

  override fun createFeedEntryParserFor(
    description: NSPIFeedDescription,
    inputStream: InputStream): NSPIFeedEntryParserType {
    throw UnsupportedOperationException()
  }

  override fun createFeedParserFor(
    description: NSPIFeedDescription,
    inputStream: InputStream): NSPIFeedParserType {

    val parser =
      this.opdsParsers.createParser(OPDS12FeedParseRequest(
      configuration = OPDS12FeedParseConfiguration(
        allowInvalidURIs = true,
        allowInvalidTimestamps = true,
        allowInvalidMIMETypes = true,
        allowInvalidIntegers = true),
      target = OPDS12FeedParseTarget.OPDS12FeedParseTargetStream(inputStream),
      acquisitionFeedEntryParsers = this.opdsFeedEntryParsers,
      uri = description.uri,
      extensionParsers = this.extensionParsers,
      extensionEntryParsers = this.extensionEntryParsers))

    return NSPIOPDS12FeedParser(parser)
  }
}
