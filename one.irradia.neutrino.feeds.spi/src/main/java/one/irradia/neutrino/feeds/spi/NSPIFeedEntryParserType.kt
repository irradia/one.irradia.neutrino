package one.irradia.neutrino.feeds.spi

import one.irradia.neutrino.feeds.model.NeutrinoFeedEntry

interface NSPIFeedEntryParserType {

  fun parse(): NSPIFeedParseResult<NeutrinoFeedEntry>

}