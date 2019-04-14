package one.irradia.neutrino.feeds.api

import one.irradia.mime.api.MIMEType
import one.irradia.neutrino.feeds.api.FeedParseResult.FeedParseFailure
import one.irradia.neutrino.feeds.api.FeedParseResult.FeedParseSuccess
import one.irradia.neutrino.feeds.model.NeutrinoFeed
import one.irradia.neutrino.feeds.model.NeutrinoFeedEntry
import one.irradia.neutrino.feeds.spi.NSPIFeedDescription
import one.irradia.neutrino.feeds.spi.NSPIFeedParseResult
import one.irradia.neutrino.feeds.spi.NSPIFeedParseResult.NSPIParseFailed
import one.irradia.neutrino.feeds.spi.NSPIFeedParseResult.NSPIParseSucceeded
import one.irradia.neutrino.feeds.spi.NSPIFeedParserProviderType
import one.irradia.neutrino.feeds.spi.NSPILexicalPosition
import one.irradia.neutrino.feeds.spi.NSPIParseError
import one.irradia.neutrino.feeds.spi.NSPIParseWarning
import org.slf4j.LoggerFactory
import java.io.InputStream
import java.net.URI
import java.util.ServiceLoader

/**
 * The default implementation of the [FeedParserType] interface.
 */

class FeedParser private constructor(
  private val parserProviders: List<NSPIFeedParserProviderType>) : FeedParserType {

  private val logger = LoggerFactory.getLogger(FeedParser::class.java)

  companion object {

    /**
     * Create a new feed parser, looking for providers using [java.util.ServiceLoader].
     */

    fun create(): FeedParserType {
      return createWithProviders(
        ServiceLoader.load(NSPIFeedParserProviderType::class.java)
          .toList())
    }

    /**
     * Create a new feed parser, using the given list of providers to parse feeds.
     */

    fun createWithProviders(parserProviders: List<NSPIFeedParserProviderType>): FeedParserType {
      return FeedParser(parserProviders.toList())
    }
  }

  private fun <T> executeTimed(toExecute: () -> T, receiveTime: (Double) -> Unit): T {
    val timeThen = System.nanoTime()
    val result = toExecute.invoke()
    val timeNow = System.nanoTime()
    val timeElapsed = timeNow - timeThen
    val timeMs = timeElapsed / 1_000_000.0
    receiveTime.invoke(timeMs)
    return result
  }

  override fun parse(
    uri: URI,
    contentType: MIMEType,
    inputStream: InputStream): FeedParseResult<NeutrinoFeed> {

    val description =
      NSPIFeedDescription(uri = uri, mimeType = contentType)

    for (provider in this.parserProviders) {
      if (provider.canSupportFeed(description)) {
        this.logger.debug(
          "selected provider {}:{} for feed {}", provider.name, provider.version, uri)
        val parser =
          provider.createFeedParserFor(description, inputStream)
        val parseResult =
          executeTimed(
            toExecute = {
              parser.parse()
            },
            receiveTime = { timeMs ->
              this.logger.debug(
                "selected provider {}:{} parsed {} in {}ms",
                provider.name,
                provider.version,
                uri,
                timeMs)
            })
        return toFeedResult(parseResult)
      }
    }

    return FeedParseFailure(
      warnings = listOf(),
      errors = listOf(FeedParseError(
        producer = "one.irradia.neutrino.feeds.api.FeedParser",
        lexical = FeedLexicalPosition(uri, 0, 0),
        message = "No providers are available that can support a feed of type ${contentType.fullType}",
        exception = null)))
  }

  override fun parseEntry(
    uri: URI,
    contentType: MIMEType,
    inputStream: InputStream): FeedParseResult<NeutrinoFeedEntry> {

    val description =
      NSPIFeedDescription(uri = uri, mimeType = contentType)

    for (provider in this.parserProviders) {
      if (provider.canSupportFeedEntry(description)) {
        this.logger.debug(
          "selected provider {}:{} for feed entry {}", provider.name, provider.version, uri)
        val parser =
          provider.createFeedEntryParserFor(description, inputStream)
        val parseResult =
          executeTimed(
            toExecute = {
              parser.parse()
            },
            receiveTime = { timeMs ->
              this.logger.debug(
                "selected provider {}:{} parsed {} in {}ms",
                provider.name,
                provider.version,
                uri,
                timeMs)
            })
        return toFeedEntryResult(parseResult)
      }
    }

    return FeedParseFailure(
      warnings = listOf(),
      errors = listOf(FeedParseError(
        producer = "one.irradia.neutrino.feeds.api.FeedParser",
        lexical = FeedLexicalPosition(uri, 0, 0),
        message = "No providers are available that can support a feed entry of type ${contentType.fullType}",
        exception = null)))
  }

  private fun toFeedEntryResult(result: NSPIFeedParseResult<NeutrinoFeedEntry>): FeedParseResult<NeutrinoFeedEntry> =
    when (result) {
      is NSPIParseSucceeded ->
        FeedParseSuccess(
          warnings = result.warnings.map(this::toWarning),
          result = result.result)
      is NSPIParseFailed ->
        FeedParseFailure(
          warnings = result.warnings.map(this::toWarning),
          errors = result.errors.map(this::toError))
    }

  private fun toFeedResult(result: NSPIFeedParseResult<NeutrinoFeed>): FeedParseResult<NeutrinoFeed> =
    when (result) {
      is NSPIParseSucceeded ->
        FeedParseSuccess(
          warnings = result.warnings.map(this::toWarning),
          result = result.result)
      is NSPIParseFailed ->
        FeedParseFailure(
          warnings = result.warnings.map(this::toWarning),
          errors = result.errors.map(this::toError))
    }

  private fun toError(error: NSPIParseError): FeedParseError =
    FeedParseError(
      producer = error.producer,
      lexical = toLexical(error.lexical),
      message = error.message,
      exception = error.exception)

  private fun toWarning(warning: NSPIParseWarning): FeedParseWarning =
    FeedParseWarning(
      producer = warning.producer,
      lexical = toLexical(warning.lexical),
      message = warning.message,
      exception = warning.exception)

  private fun toLexical(lexical: NSPILexicalPosition): FeedLexicalPosition =
    FeedLexicalPosition(
      line = lexical.line,
      column = lexical.column,
      source = lexical.source)

}