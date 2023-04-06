package com.dreamsoftware.artcollectibles.data.memory.datasource

import com.dreamsoftware.artcollectibles.data.memory.datasource.core.IMemoryCacheDataSource
import com.dreamsoftware.artcollectibles.domain.models.UserInfo

interface IUserMemoryDataSource: IMemoryCacheDataSource<String, UserInfo>