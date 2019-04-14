package one.irradia.neutrino.feeds.api

import one.irradia.http.api.HTTPAuthentication
import one.irradia.http.api.HTTPClientType
import one.irradia.http.api.HTTPResult
import one.irradia.mime.api.MIMEType
import one.irradia.neutrino.feeds.api.FeedLoadResult.FeedLoadFailure.FeedLoadFailureParse
import one.irradia.neutrino.feeds.api.FeedLoadResult.FeedLoadFailure.FeedLoadFailureTransport.FeedLoadFailureTransportGeneric
import one.irradia.neutrino.feeds.api.FeedLoadResult.FeedLoadFailure.FeedLoadFailureTransport.FeedLoadFailureTransportHTTP
import one.irradia.neutrino.feeds.api.FeedLoadResult.FeedLoadSuccess
import one.irradia.neutrino.feeds.api.FeedParseResult.FeedParseFailure
import one.irradia.neutrino.feeds.api.FeedParseResult.FeedParseSuccess
import one.irradia.neutrino.feeds.model.NeutrinoFeed
import one.irradia.neutrino.feeds.model.NeutrinoFeedEntry
import java.io.InputStream
import java.net.URI

/**
 * The default implementation of the [FeedLoaderType] interface.
 */

class FeedLoader private constructor(
  private val mimeParsers: (String) -> MIMEType,
  private val feedParser: FeedParserType,
  private val httpClient: HTTPClientType) : FeedLoaderType {

  companion object {

    /**
     * Create a new feed loader.
     */

    fun create(
      mimeParsers: (String) -> MIMEType,
      feedParser: FeedParserType,
      httpClient: HTTPClientType): FeedLoaderType =
      FeedLoader(
        mimeParsers = mimeParsers,
        feedParser = feedParser,
        httpClient = httpClient)

  }

  override fun loadFeed(
    uri: URI,
    method: String,
    authentication: (URI) -> HTTPAuthentication?): FeedLoadResult<NeutrinoFeed> =
    this.execute(
      uri = uri,
      method = method,
      authentication = authentication,
      onSuccess = { response ->
        this.parseFeed(
          uri = uri,
          contentType = response.contentTypeOrDefault,
          data = response.result)
      })

  override fun loadFeedEntry(
    uri: URI,
    method: String,
    authentication: (URI) -> HTTPAuthentication?): FeedLoadResult<NeutrinoFeedEntry> =
    this.execute(
      uri = uri,
      method = method,
      authentication = authentication,
      onSuccess = { response ->
        this.parseFeedEntry(
          uri = uri,
          contentType = response.contentTypeOrDefault,
          data = response.result)
      })

  private fun <T> execute(
    uri: URI,
    method: String,
    authentication: (URI) -> HTTPAuthentication?,
    onSuccess: (HTTPResult.HTTPOK<InputStream>) -> FeedLoadResult<T>): FeedLoadResult<T> {

    return when (val response =
      this.httpClient.request(
        uri = uri,
        method = method,
        authentication = authentication,
        offset = 0L)) {

      is HTTPResult.HTTPOK ->
        onSuccess.invoke(response)

      is HTTPResult.HTTPFailed.HTTPError ->
        FeedLoadFailureTransportHTTP(
          message = response.message,
          statusCode = response.statusCode,
          exception = null)

      is HTTPResult.HTTPFailed.HTTPFailure ->
        FeedLoadFailureTransportGeneric(
          exception = response.exception)
    }
  }

  private fun parseMIMEType(
    uri: URI,
    contentType: String): FeedLoadResult<MIMEType> {
    try {
      return FeedLoadSuccess(result = this.mimeParsers.invoke(contentType))
    } catch (e: Exception) {
      return FeedLoadFailureParse(
        warnings = listOf(),
        errors = listOf(FeedParseError(
          producer = "one.irradia.mime.api.MIMEParserType",
          lexical = FeedLexicalPosition(uri, 0, 0),
          message = e.message ?: "Could not parse MIME type",
          exception = e
        )))
    }
  }

  private fun parseFeedEntry(
    uri: URI,
    contentType: String,
    data: InputStream): FeedLoadResult<NeutrinoFeedEntry> =
    parseMIMEType(uri, contentType)
      .flatMap { mime ->
        this.mapResult(
          this.feedParser.parseEntry(
            uri = uri,
            contentType = mime,
            inputStream = data))
      }

  private fun <T> mapResult(result: FeedParseResult<T>): FeedLoadResult<T> =
    when (result) {
      is FeedParseSuccess ->
        FeedLoadSuccess(warnings = result.warnings, result = result.result)
      is FeedParseFailure ->
        FeedLoadFailureParse(warnings = result.warnings, errors = result.errors)
    }

  private fun parseFeed(
    uri: URI,
    contentType: String,
    data: InputStream): FeedLoadResult<NeutrinoFeed> =
    parseMIMEType(uri, contentType)
      .flatMap { mime ->
        this.mapResult(
          this.feedParser.parse(
            uri = uri,
            contentType = mime,
            inputStream = data))
      }

}