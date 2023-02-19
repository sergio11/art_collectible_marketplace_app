package com.dreamsoftware.artcollectibles.data.local.room.dao.core

import androidx.room.*
import androidx.room.OnConflictStrategy
import androidx.room.Update


/**
 * Support DAO
 */
interface ISupportDAO<T> {

    /**
     * Insert
     * @param entity
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(vararg entity: T): LongArray

    /**
     * Insert an object in the database.
     *
     * @param entity the object to be inserted.
     * @return The SQLite row
     */
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    @Transaction
    fun insert(entity: T): Long

    /**
     * Insert an array of objects in the database.
     *
     * @param entityList the objects to be inserted.
     * @return The SQLite row ids
     */
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    @Transaction
    fun insert(entityList: List<T>): List<Long>


    /**
     * Update an object from the database.
     *
     * @param entity the object to be updated
     */
    @Update
    fun update(entity: T)


    /**
     * Update an array of objects from the database.
     *
     * @param entityList the object to be updated
     */
    @Update
    @Transaction
    fun update(entityList: List<T>)

    /**
     * Delete
     * @param entity
     */
    @Delete
    fun delete(entity: T)

    /**
     * Find All
     */
    fun findAll(): List<T>

    /**
     * Count
     */
    fun count(): Int

    /**
     *
     */
    @Transaction
    fun upsert(entity: T)

    /**
     *
     */
    @Transaction
    fun upsert(entityList: List<T>)
}