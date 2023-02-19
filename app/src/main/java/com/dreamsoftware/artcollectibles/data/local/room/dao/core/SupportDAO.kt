package com.dreamsoftware.artcollectibles.data.local.room.dao.core

import androidx.room.RawQuery
import androidx.sqlite.db.SimpleSQLiteQuery
import androidx.sqlite.db.SupportSQLiteQuery
import java.lang.reflect.ParameterizedType

/**
 * Support DAO
 **/
abstract class SupportDAO<T> : ISupportDAO<T> {

    /**
     * Find All
     */
    override fun findAll(): List<T> {
        val query = SimpleSQLiteQuery(
            "select * from ${getTableName()}"
        )
        return findAll(query)
    }

    /**
     * Count
     */
    override fun count(): Int {
        val query = SimpleSQLiteQuery(
            "select count(*) from ${getTableName()}"
        )
        return count(query)
    }

    /**
     *
     */
    override fun upsert(entity: T) {
        val id = insert(entity)
        if (id == -1L) {
            update(entity)
        }
    }

    /**
     *
     */
    override fun upsert(entityList: List<T>) {
        val insertResult = insert(entityList)
        val updateList = ArrayList<T>()

        for (i in insertResult.indices) {
            if (insertResult[i] == -1L) {
                updateList.add(entityList[i])
            }
        }

        if (updateList.isNotEmpty()) {
            update(updateList)
        }
    }

    /**
     *
     * Private Methods
     *
     */


    /**
     * Find All
     * @param query
     */
    @RawQuery
    protected abstract fun findAll(query: SupportSQLiteQuery): List<T>

    /**
     * Count
     * @param query
     */
    @RawQuery
    protected abstract fun count(query: SupportSQLiteQuery): Int

    /**
     * Get Table Name
     */
    private fun getTableName(): String {
        val clazz = (javaClass.superclass!!.genericSuperclass as ParameterizedType)
            .actualTypeArguments[0] as Class<*>
        return clazz.simpleName
    }

}