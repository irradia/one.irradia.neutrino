package one.irradia.neutrino.feeds.api

import one.irradia.http.api.HTTPAuthentication
import one.irradia.neutrino.feeds.model.NeutrinoFeed
import one.irradia.neutrino.feeds.model.NeutrinoFeedEntry
import java.net.URI

/**
 * The type of synchronous feed loaders.
 *
 * Implementations are encouraged _not_ to cache the results of feed loading, but to instead
 * assume that callers are responsible for caching successfully loaded feeds if necessary.
 */

interface FeedLoaderType {

  /**
   * Load a feed from the given URI.
   */

  fun loadFeed(
    uri: URI,
    method: String = "GET",
    authentication: (URI) -> HTTPAuthentication? = { null }): FeedLoadResult<NeutrinoFeed>

  /**
   * Load a feed entry from the given URI.
   */

  fun loadFeedEntry(
    uri: URI,
    method: String = "GET",
    authentication: (URI) -> HTTPAuthentication? = { null }): FeedLoadResult<NeutrinoFeedEntry>

}
