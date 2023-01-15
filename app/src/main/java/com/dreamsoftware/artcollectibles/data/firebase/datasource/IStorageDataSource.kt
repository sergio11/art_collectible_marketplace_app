package com.dreamsoftware.artcollectibles.data.firebase.datasource

import android.net.Uri
import com.dreamsoftware.artcollectibles.data.firebase.exception.FileNotFoundException
import com.dreamsoftware.artcollectibles.data.firebase.exception.SaveFileException

interface IStorageDataSource {

    /**
     * Save file
     * @param directoryName
     * @param name
     * @param fileUri
     */
    @Throws(SaveFileException::class)
    suspend fun save(directoryName: String, name: String, fileUri: String): Uri

    /**
     * Get file information
     * @param directoryName
     * @param name
     */
    @Throws(FileNotFoundException::class)
    suspend fun get(directoryName: String, name: String): Uri

    /**
     * Remove file
     * @param directoryName
     * @param name
     */
    @Throws(FileNotFoundException::class)
    suspend fun remove(directoryName: String, name: String)

}