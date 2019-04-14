package one.irradia.neutrino.feeds.api

import one.irradia.mime.api.MIMEType
import one.irradia.neutrino.feeds.model.NeutrinoFeed
import one.irradia.neutrino.feeds.model.NeutrinoFeedEntry
import java.io.InputStream
import java.net.URI

/**
 * A synchronous feed parser interface.
 */

interface FeedParserType {

  /**
   * Attempt to parse a full feed from the given input stream, assuming that the feed came
   * from `uri` and has the given content type.
   */

  fun parse(
    uri: URI,
    contentType: MIMEType,
    inputStream: InputStream): FeedParseResult<NeutrinoFeed>

  /**
   * Attempt to parse a single feed entry from the given input stream, assuming that the feed came
   * from `uri` and has the given content type.
   */

  fun parseEntry(
    uri: URI,
    contentType: MIMEType,
    inputStream: InputStream): FeedParseResult<NeutrinoFeedEntry>

}
