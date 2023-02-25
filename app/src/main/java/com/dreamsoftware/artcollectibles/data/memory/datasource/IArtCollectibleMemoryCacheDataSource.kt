package com.dreamsoftware.artcollectibles.data.memory.datasource

import com.dreamsoftware.artcollectibles.data.memory.datasource.core.IMemoryCacheDataSource
import com.dreamsoftware.artcollectibles.domain.models.ArtCollectible

interface IArtCollectibleMemoryCacheDataSource:
    IMemoryCacheDataSource<Any, Iterable<ArtCollectible>>