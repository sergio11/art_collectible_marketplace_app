package com.dreamsoftware.artcollectibles.data.firebase.datasource

import android.net.Uri
import com.dreamsoftware.artcollectibles.data.firebase.exception.FileNotFoundException
import com.dreamsoftware.artcollectibles.data.firebase.exception.SaveFileException

interface IStorageDataSource {

    /**
     * Save file
     * @param name
     * @param fileUri
     */
    @Throws(SaveFileException::class)
    suspend fun save(name: String, fileUri: String): Uri

    /**
     * Get file information
     * @param name
     */
    @Throws(FileNotFoundException::class)
    suspend fun get(name: String): Uri

}