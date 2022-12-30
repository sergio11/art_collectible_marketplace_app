package com.dreamsoftware.artcollectibles.data.firebase.datasource.impl

import android.net.Uri
import com.dreamsoftware.artcollectibles.data.firebase.datasource.IStorageDataSource
import com.dreamsoftware.artcollectibles.data.firebase.exception.FileNotFoundException
import com.dreamsoftware.artcollectibles.data.firebase.exception.SaveFileException
import com.google.firebase.storage.FirebaseStorage
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext

/**
 * Storage Data Source Impl
 * @param firebaseStorage
 */
internal class StorageDataSourceImpl(
    private val firebaseStorage: FirebaseStorage
) : IStorageDataSource {

    /**
     * Save file
     * @param name
     * @param fileUri
     */
    @Throws(SaveFileException::class)
    override suspend fun save(name: String, fileUri: String): Uri = withContext(Dispatchers.IO) {
        try {
            with(firebaseStorage.reference) {
                child(name).putFile(Uri.parse(fileUri)).await()
                child(name).downloadUrl.await()
            }
        } catch (ex: Exception) {
            throw SaveFileException("An error occurred when trying to save a new file", ex)
        }
    }

    /**
     * Get file information
     * @param name
     */
    @Throws(FileNotFoundException::class)
    override suspend fun get(name: String): Uri = withContext(Dispatchers.IO) {
        try {
            firebaseStorage.reference
                .child(name)
                .downloadUrl
                .await()
        } catch (ex: Exception) {
            throw FileNotFoundException("An error occurred when trying to get file information", ex)
        }
    }
}