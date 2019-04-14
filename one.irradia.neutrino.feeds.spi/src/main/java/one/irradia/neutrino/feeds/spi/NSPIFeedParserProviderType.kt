package one.irradia.neutrino.feeds.spi

import java.io.InputStream

interface NSPIFeedParserProviderType {

  val name: String

  val version: NSPIVersion

  fun canSupportFeed(
    description: NSPIFeedDescription): Boolean

  fun canSupportFeedEntry(
    description: NSPIFeedDescription): Boolean

  fun createFeedParserFor(
    description: NSPIFeedDescription,
    inputStream: InputStream): NSPIFeedParserType

  fun createFeedEntryParserFor(
    description: NSPIFeedDescription,
    inputStream: InputStream): NSPIFeedEntryParserType

}
