package one.irradia.neutrino.tests.feeds

import com.google.common.util.concurrent.ListeningExecutorService
import com.google.common.util.concurrent.MoreExecutors
import one.irradia.http.vanilla.HTTPClientsOkHTTP
import one.irradia.mime.vanilla.MIMEParser
import one.irradia.neutrino.feeds.api.FeedLoader
import one.irradia.neutrino.feeds.api.FeedLoaderAsyncType
import one.irradia.neutrino.feeds.api.FeedLoaderType
import one.irradia.neutrino.feeds.api.FeedParser
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.slf4j.Logger
import java.net.URI
import java.util.concurrent.Executors
import java.util.concurrent.TimeUnit

abstract class FeedLoaderAsyncContract {

  protected abstract fun logger(): Logger

  protected abstract fun loader(
    executor: ListeningExecutorService,
    feedLoader: FeedLoaderType): FeedLoaderAsyncType

  private lateinit var feedLoader: FeedLoaderType
  private lateinit var executor: ListeningExecutorService
  private lateinit var logger: Logger

  @Before
  fun testSetup() {
    this.logger = this.logger()

    this.feedLoader =
      FeedLoader.create(
        mimeParsers = { text -> MIMEParser.parseRaisingException(text) },
        feedParser = FeedParser.create(),
        httpClient = HTTPClientsOkHTTP().createClient("one.irradia.neutrino"))

    this.executor =
      MoreExecutors.listeningDecorator(Executors.newSingleThreadExecutor())
  }

  @After
  fun testTearDown() {
    this.executor.shutdown()
  }

}