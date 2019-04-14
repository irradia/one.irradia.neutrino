package one.irradia.neutrino.feeds.api

import com.google.common.cache.Cache
import com.google.common.cache.CacheBuilder
import com.google.common.util.concurrent.ListenableFuture
import com.google.common.util.concurrent.ListeningExecutorService
import one.irradia.http.api.HTTPAuthentication
import one.irradia.neutrino.feeds.model.NeutrinoFeed
import org.slf4j.LoggerFactory
import java.net.URI
import java.util.concurrent.Callable
import java.util.concurrent.TimeUnit

/**
 * The default implementation of the [FeedLoaderAsyncType] interface.
 */

class FeedLoaderAsync private constructor(
  private val executor: ListeningExecutorService,
  private val feedLoader: FeedLoaderType,
  private val cache: Cache<URI, NeutrinoFeed>) : FeedLoaderAsyncType {

  private val logger = LoggerFactory.getLogger(FeedLoaderAsync::class.java)
  private val cacheMap = this.cache.asMap()

  companion object {

    private fun createCache(
      concurrencyLevel: Int,
      cacheExpiration: Pair<Long, TimeUnit>): Cache<URI, NeutrinoFeed> =
      CacheBuilder.newBuilder()
        .concurrencyLevel(concurrencyLevel)
        .expireAfterWrite(cacheExpiration.first, cacheExpiration.second)
        .build<URI, NeutrinoFeed>()

    /**
     * Create a new asynchronous feed loader.
     */

    fun create(
      executor: ListeningExecutorService,
      feedLoader: FeedLoaderType,
      cacheConcurrency: Int = 2,
      cacheExpiration: Pair<Long, TimeUnit> = Pair(5L, TimeUnit.MINUTES)): FeedLoaderAsyncType =
      FeedLoaderAsync(
        executor = executor,
        feedLoader = feedLoader,
        cache = createCache(cacheConcurrency, cacheExpiration))
  }

  override fun loadAsync(
    uri: URI,
    method: String,
    authentication: (URI) -> HTTPAuthentication?,
    cache: Boolean): ListenableFuture<FeedLoadResult<NeutrinoFeed>> =
    this.executor.submit(Callable { execute(cache, uri, method, authentication) })

  private fun execute(
    cache: Boolean,
    uri: URI,
    method: String,
    authentication: (URI) -> HTTPAuthentication?): FeedLoadResult<NeutrinoFeed> {

    if (cache) {
      val cached = this.cacheMap[uri]
      if (cached != null) {
        this.logger.debug("{}: reused cached value", uri)
        return FeedLoadResult.FeedLoadSuccess(warnings = listOf(), result = cached)
      }
    }

    val result = this.feedLoader.loadFeed(uri, method, authentication)

    if (cache) {
      if (result is FeedLoadResult.FeedLoadSuccess) {
        this.logger.debug("{}: updated cache with feed", uri)
        this.cache.put(uri, result.result)
      }
    }

    return result
  }
}