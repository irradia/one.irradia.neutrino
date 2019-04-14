package one.irradia.neutrino.tests.local

import com.google.common.util.concurrent.ListeningExecutorService
import one.irradia.neutrino.feeds.api.FeedLoaderAsync
import one.irradia.neutrino.feeds.api.FeedLoaderAsyncType
import one.irradia.neutrino.feeds.api.FeedLoaderType
import one.irradia.neutrino.tests.feeds.FeedLoaderAsyncContract
import org.slf4j.Logger
import org.slf4j.LoggerFactory

class FeedLoaderAsyncTest : FeedLoaderAsyncContract() {

  override fun logger(): Logger =
    LoggerFactory.getLogger(FeedLoaderAsyncTest::class.java)

  override fun loader(
    executor: ListeningExecutorService,
    feedLoader: FeedLoaderType): FeedLoaderAsyncType =
    FeedLoaderAsync.create(executor, feedLoader)

}