package com.dreamsoftware.artcollectibles.data.firebase.datasource

import com.dreamsoftware.artcollectibles.data.firebase.exception.*

interface IFollowersDataSource {

    @Throws(CheckFollowerException::class)
    suspend fun isFollowedBy(from: String, to: String): Boolean

    @Throws(GetFollowersException::class)
    suspend fun getFollowers(userId: String): List<String>

    @Throws(GetFollowingException::class)
    suspend fun getFollowing(userId: String): List<String>

    @Throws(CountFollowersException::class)
    suspend fun countFollowers(userId: String): Long

    @Throws(CountFollowingException::class)
    suspend fun countFollowing(userId: String): Long

    @Throws(AddFollowerException::class)
    suspend fun follow(from: String, to: String)

    @Throws(RemoveFollowerException::class)
    suspend fun unfollow(from: String, to: String)
}