package one.irradia.neutrino.tests.feeds

import one.irradia.neutrino.feeds.opds12.NSPIOPDS12FeedParsersService
import org.junit.Before
import org.junit.Test
import org.slf4j.Logger

abstract class NSPIOPDS12FeedParsersContract {

  protected abstract fun logger(): Logger

  private lateinit var logger : Logger

  @Before
  fun testSetup()
  {
    this.logger = this.logger()
  }

  @Test
  fun testInitialization()
  {
    val parsers = NSPIOPDS12FeedParsersService()
    this.logger.debug("{}", parsers.name)
    this.logger.debug("{}", parsers.version)
  }
}