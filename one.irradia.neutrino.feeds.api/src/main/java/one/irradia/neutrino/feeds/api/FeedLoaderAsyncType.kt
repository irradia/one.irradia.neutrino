package one.irradia.neutrino.feeds.api

import com.google.common.util.concurrent.ListenableFuture
import one.irradia.http.api.HTTPAuthentication
import one.irradia.neutrino.feeds.model.NeutrinoFeed
import java.net.URI

/**
 * The type of asynchronous feed loaders.
 */

interface FeedLoaderAsyncType {

  /**
   * Load a feed from the given URI.
   */

  fun loadAsync(
    uri: URI,
    method: String = "GET",
    authentication: (URI) -> HTTPAuthentication? = { null },
    cache: Boolean = false): ListenableFuture<FeedLoadResult<NeutrinoFeed>>

}
