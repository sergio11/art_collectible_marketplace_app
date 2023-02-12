package com.dreamsoftware.artcollectibles.domain.models

data class ArtCollectibleMintedEvent(
    val artCollectible: ArtCollectible
): IEvent
