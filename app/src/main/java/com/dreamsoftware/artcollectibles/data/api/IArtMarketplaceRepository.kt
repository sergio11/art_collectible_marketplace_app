package com.dreamsoftware.artcollectibles.data.api

import com.dreamsoftware.artcollectibles.domain.models.ArtCollectibleForSale
import com.dreamsoftware.artcollectibles.domain.models.UserWalletCredentials

interface IArtMarketplaceRepository {

    /**
     * Fetch non sold and non canceled market items
     */
    suspend fun fetchAvailableMarketItems(userWalletCredentials: UserWalletCredentials): Iterable<ArtCollectibleForSale>

    /**
     * Fetch market items that are being listed by the current authenticated user
     */
    suspend fun fetchSellingMarketItems(userWalletCredentials: UserWalletCredentials): Iterable<ArtCollectibleForSale>

    /**
     * Fetch market items that are owned by the current authenticated user
     */
    suspend fun fetchOwnedMarketItems(userWalletCredentials: UserWalletCredentials): Iterable<ArtCollectibleForSale>

    /**
     * Allow us to fetch market history
     */
    suspend fun fetchMarketHistory(userWalletCredentials: UserWalletCredentials): Iterable<ArtCollectibleForSale>
}