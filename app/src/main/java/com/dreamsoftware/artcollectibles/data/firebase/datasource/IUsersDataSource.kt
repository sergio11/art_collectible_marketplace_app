package com.dreamsoftware.artcollectibles.data.firebase.datasource

import com.dreamsoftware.artcollectibles.data.firebase.exception.SaveUserException
import com.dreamsoftware.artcollectibles.data.firebase.exception.UserErrorException
import com.dreamsoftware.artcollectibles.data.firebase.exception.UserNotFoundException
import com.dreamsoftware.artcollectibles.data.firebase.model.SaveUserDTO
import com.dreamsoftware.artcollectibles.data.firebase.model.UserDTO

interface IUsersDataSource {

    /**
     * Create user information
     * @param user
     */
    @Throws(SaveUserException::class)
    suspend fun save(user: SaveUserDTO)

    /**
     * @param uid
     */
    @Throws(UserNotFoundException::class, UserErrorException::class)
    suspend fun getById(uid: String): UserDTO

    /**
     * @param uidList
     */
    @Throws(UserErrorException::class)
    suspend fun getById(uidList: Iterable<String>): Iterable<UserDTO>

    /**
     * Get user by address
     * @param userAddress
     */
    @Throws(UserNotFoundException::class, UserErrorException::class)
    suspend fun getByAddress(userAddress: String): UserDTO

    /**
     * Get All users
     */
    @Throws(UserErrorException::class)
    suspend fun getAll(): Iterable<UserDTO>

    /**
     * Find Users by name
     * @param term
     */
    @Throws(UserErrorException::class)
    suspend fun findUsersByName(term: String): Iterable<UserDTO>

}