package com.dreamsoftware.artcollectibles.data.firebase.datasource.impl

import com.dreamsoftware.artcollectibles.data.firebase.datasource.IUsersDataSource
import com.dreamsoftware.artcollectibles.data.firebase.exception.FirebaseException
import com.dreamsoftware.artcollectibles.data.firebase.exception.SaveUserException
import com.dreamsoftware.artcollectibles.data.firebase.exception.UserErrorException
import com.dreamsoftware.artcollectibles.data.firebase.exception.UserNotFoundException
import com.dreamsoftware.artcollectibles.data.firebase.mapper.SaveUserMapper
import com.dreamsoftware.artcollectibles.data.firebase.mapper.UserMapper
import com.dreamsoftware.artcollectibles.data.firebase.model.SaveUserDTO
import com.dreamsoftware.artcollectibles.data.firebase.model.UserDTO
import com.google.firebase.firestore.FieldPath
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.SetOptions
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext

/**
 * User Data Source Impl
 * @param userMapper
 * @param saveUserMapper
 * @param firebaseStore
 */
internal class UsersDataSourceImpl(
    private val userMapper: UserMapper,
    private val saveUserMapper: SaveUserMapper,
    private val firebaseStore: FirebaseFirestore
) : IUsersDataSource {

    private companion object {
        const val USERS_COLLECTION_NAME = "users"
    }

    @Throws(SaveUserException::class)
    override suspend fun save(user: SaveUserDTO): Unit = withContext(Dispatchers.IO) {
        try {
            firebaseStore.collection(USERS_COLLECTION_NAME)
                .document(user.uid)
                .set(saveUserMapper.mapInToOut(user), SetOptions.merge())
                .await()
        } catch (ex: Exception) {
            throw SaveUserException("An error occurred when trying to save user information", ex)
        }
    }

    @Throws(UserNotFoundException::class, UserErrorException::class)
    override suspend fun getById(uid: String): UserDTO = withContext(Dispatchers.IO) {
        try {
            firebaseStore.collection(USERS_COLLECTION_NAME)
                .document(uid).get().await()?.data?.let {
                    userMapper.mapInToOut(it)
                } ?: throw UserNotFoundException("User not found")
        } catch (ex: FirebaseException) {
            throw ex
        } catch (ex: Exception) {
            throw UserErrorException("An error occurred when trying to get user information", ex)
        }
    }

    @Throws(UserErrorException::class)
    override suspend fun getById(uidList: Iterable<String>): Iterable<UserDTO> = withContext(Dispatchers.IO) {
        try {
            firebaseStore.collection(USERS_COLLECTION_NAME)
                .whereIn(FieldPath.documentId(), uidList.toList())
                .get().await()
                .documents.mapNotNull { it.data }
                .map { userMapper.mapInToOut(it) }
        } catch (ex: FirebaseException) {
            throw ex
        } catch (ex: Exception) {
            throw UserErrorException("An error occurred when trying to get user information", ex)
        }
    }

    @Throws(UserNotFoundException::class, UserErrorException::class)
    override suspend fun getByAddress(userAddress: String): UserDTO = withContext(Dispatchers.IO) {
        try {
            firebaseStore.collection(USERS_COLLECTION_NAME)
                .whereEqualTo("walletAddress", userAddress)
                .get().await()?.documents?.firstOrNull()?.data?.let {
                    userMapper.mapInToOut(it)
                } ?: throw UserNotFoundException("User not found")
        } catch (ex: FirebaseException) {
            throw ex
        } catch (ex: Exception) {
            throw UserErrorException("An error occurred when trying to get user information", ex)
        }
    }

    /**
     * Get All
     */
    @Throws(UserErrorException::class)
    override suspend fun getAll(): Iterable<UserDTO> = withContext(Dispatchers.IO) {
        firebaseStore.collection(USERS_COLLECTION_NAME).get()
            .await().documents.mapNotNull { it.data }
            .map { userMapper.mapInToOut(it) }
    }

    /**
     * Find Users by name
     * @param term
     */
    @Throws(UserErrorException::class)
    override suspend fun findUsersByName(term: String): Iterable<UserDTO> =
        withContext(Dispatchers.IO) {
            firebaseStore.collection(USERS_COLLECTION_NAME)
                .whereNotEqualTo("isPublicProfile", "false")
                .whereGreaterThanOrEqualTo("name", term)
                .whereLessThanOrEqualTo("name", term + "\uf8ff")
                .get()
                .await().documents.mapNotNull { it.data }
                .map { userMapper.mapInToOut(it) }
        }
}