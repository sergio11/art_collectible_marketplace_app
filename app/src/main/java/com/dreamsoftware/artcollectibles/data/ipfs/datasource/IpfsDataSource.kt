package com.dreamsoftware.artcollectibles.data.ipfs.datasource

import com.dreamsoftware.artcollectibles.data.ipfs.models.CreateTokenMetadataDTO
import com.dreamsoftware.artcollectibles.data.ipfs.models.TokenMetadataDTO
import com.dreamsoftware.artcollectibles.data.ipfs.models.UpdateTokenMetadataDTO

interface IpfsDataSource {

    /**
     * Save token metadata into IPFS
     * @param tokenMetadata
     */
    suspend fun create(tokenMetadata: CreateTokenMetadataDTO): TokenMetadataDTO

    /**
     * Update Metadata
     * @param metadata
     */
    suspend fun update(metadata: UpdateTokenMetadataDTO)

    /**
     * Delete File by CID
     * @param cid
     */
    suspend fun delete(cid: String)

    /**
     * Fetch file by CID
     * @param cid
     */
    suspend fun fetchByCid(cid: String): TokenMetadataDTO

    /**
     * Fetch By Creator Address
     * @param creatorAddress
     */
    suspend fun fetchByCreatorAddress(creatorAddress: String): Iterable<TokenMetadataDTO>
}