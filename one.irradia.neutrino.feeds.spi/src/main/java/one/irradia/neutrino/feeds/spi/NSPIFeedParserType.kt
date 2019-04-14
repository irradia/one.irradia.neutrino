package one.irradia.neutrino.feeds.spi

import one.irradia.neutrino.feeds.model.NeutrinoFeed

interface NSPIFeedParserType {

  fun parse(): NSPIFeedParseResult<NeutrinoFeed>

}