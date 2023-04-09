package com.dreamsoftware.artcollectibles.data.memory.datasource

import com.dreamsoftware.artcollectibles.data.memory.datasource.core.IMemoryCacheDataSource
import org.web3j.crypto.Credentials

interface IWalletCredentialsMemoryDataSource: IMemoryCacheDataSource<String, Credentials>