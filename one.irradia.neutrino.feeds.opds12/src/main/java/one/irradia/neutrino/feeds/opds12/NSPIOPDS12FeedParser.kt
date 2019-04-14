package one.irradia.neutrino.feeds.opds12

import one.irradia.neutrino.feeds.model.NeutrinoFeed
import one.irradia.neutrino.feeds.spi.NSPIFeedParseResult
import one.irradia.neutrino.feeds.spi.NSPIParseError
import one.irradia.neutrino.feeds.spi.NSPIFeedParseResult.NSPIParseFailed
import one.irradia.neutrino.feeds.spi.NSPIParseWarning
import one.irradia.neutrino.feeds.spi.NSPIFeedParserType
import one.irradia.neutrino.feeds.spi.NSPILexicalPosition
import one.irradia.opds1_2.api.OPDS12Feed
import one.irradia.opds1_2.api.OPDS12ParseResult.OPDS12ParseError
import one.irradia.opds1_2.api.OPDS12ParseResult.OPDS12ParseFailed
import one.irradia.opds1_2.api.OPDS12ParseResult.OPDS12ParseSucceeded
import one.irradia.opds1_2.api.OPDS12ParseResult.OPDS12ParseWarning
import one.irradia.opds1_2.lexical.OPDS12LexicalPosition
import one.irradia.opds1_2.parser.api.OPDS12FeedParserType

internal class NSPIOPDS12FeedParser(
  private val opdsParser: OPDS12FeedParserType) : NSPIFeedParserType {

  override fun parse(): NSPIFeedParseResult<NeutrinoFeed> =
    when (val data = this.opdsParser.parse()) {
      is OPDS12ParseSucceeded ->
        mapFeed(data.warnings, data.result)
      is OPDS12ParseFailed ->
        NSPIParseFailed(
          warnings = data.warnings.map(this::toNSPIWarning),
          errors = data.errors.map(this::toNSPIError))
    }

  private fun mapFeed(
    warnings: List<OPDS12ParseWarning>,
    inputFeed: OPDS12Feed): NSPIFeedParseResult<NeutrinoFeed> {

    val feed =
      NeutrinoFeed(
        uri = inputFeed.uri,
        title = inputFeed.title)

    return NSPIFeedParseResult.NSPIParseSucceeded(
      warnings = warnings.map(this::toNSPIWarning),
      result = feed)
  }

  private fun toNSPIWarning(warning: OPDS12ParseWarning): NSPIParseWarning =
    NSPIParseWarning(
      producer = warning.producer,
      lexical = toNSPILexical(warning.lexical),
      message = warning.message,
      exception = warning.exception)

  private fun toNSPIError(error: OPDS12ParseError): NSPIParseError =
    NSPIParseError(
      producer = error.producer,
      lexical = toNSPILexical(error.lexical),
      message = error.message,
      exception = error.exception)

  private fun toNSPILexical(lexical: OPDS12LexicalPosition): NSPILexicalPosition =
    NSPILexicalPosition(
      source = lexical.source,
      line = lexical.line,
      column = lexical.column)
}

