package one.irradia.neutrino.views

sealed class NCatalogEvent : NeutrinoEventType {

  object NCatalogCollectionSelectOpenRequested : NCatalogEvent()

  data class NCatalogFeedRequested(
    val arguments: NPageFeedArguments)
    : NCatalogEvent()

}