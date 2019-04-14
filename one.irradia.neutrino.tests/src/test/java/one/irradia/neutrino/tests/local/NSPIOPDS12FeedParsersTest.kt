package one.irradia.neutrino.tests.local

import one.irradia.neutrino.tests.feeds.NSPIOPDS12FeedParsersContract
import org.slf4j.Logger
import org.slf4j.LoggerFactory

class NSPIOPDS12FeedParsersTest : NSPIOPDS12FeedParsersContract() {

  override fun logger(): Logger =
    LoggerFactory.getLogger(NSPIOPDS12FeedParsersTest::class.java)

}