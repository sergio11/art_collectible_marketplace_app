package com.dreamsoftware.artcollectibles.data.api

import com.dreamsoftware.artcollectibles.domain.models.ArtCollectible
import com.dreamsoftware.artcollectibles.domain.models.UserWalletCredentials
import java.math.BigInteger

interface IArtCollectibleRepository {

    /**
     * Allows you to retrieve the list of tokens owned
     * @param credentials
     */
    suspend fun getTokensOwnedBy(credentials: UserWalletCredentials): Iterable<ArtCollectible>

    /**
     * Allows you to retrieve the list of tokens created
     * @param credentials
     */
    suspend fun getTokensCreatedBy(credentials: UserWalletCredentials): Iterable<ArtCollectible>

    /**
     * Retrieve token information by id
     */
    suspend fun getTokenById(tokenId: BigInteger, credentials: UserWalletCredentials): ArtCollectible
}