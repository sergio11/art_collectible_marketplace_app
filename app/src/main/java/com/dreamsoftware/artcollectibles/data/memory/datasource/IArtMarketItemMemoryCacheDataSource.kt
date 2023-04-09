package com.dreamsoftware.artcollectibles.data.memory.datasource

import com.dreamsoftware.artcollectibles.data.memory.datasource.core.IMemoryCacheDataSource
import com.dreamsoftware.artcollectibles.domain.models.ArtCollectibleForSale

interface IArtMarketItemMemoryCacheDataSource:
    IMemoryCacheDataSource<Any, ArtCollectibleForSale>