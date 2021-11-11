package com.sinfotek.component.db.dao

import androidx.room.*
import kotlinx.coroutines.flow.Flow

/**
 *
 * @author Y&N
 * date: 2021/11/10
 * desc:
 */
@Dao
interface BaseDao<T> {
    //onConflict：默认值是OnConflictStrategy.ABORT，表示当插入有冲突的时候的处理策略。OnConflictStrategy封装了Room解决冲突的相关策略：
    //1. OnConflictStrategy.REPLACE：冲突策略是取代旧数据同时继续事务。
    //
    //2. OnConflictStrategy.ROLLBACK：冲突策略是回滚事务。
    //
    //3. OnConflictStrategy.ABORT：冲突策略是终止事务。
    //
    //4. OnConflictStrategy.FAIL：冲突策略是事务失败。
    //
    //5. OnConflictStrategy.IGNORE：冲突策略是忽略冲突。
    /**
     * 插入数据
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAll(list: MutableList<T>) : Array<Long>

    /**
     * 插入数据
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAll(vararg elements: T): Array<Long>

    /**
     * 删除数据
     */
    @Delete
    fun delete(element: T) :Int

    /**
     * 删除多个数据
     */
    @Delete
    fun deleteList(elements: MutableList<T>) : Int

    /**
     * 删除多个数据
     */
    @Delete
    fun deleteSome(vararg elements: T) :Int

    /**
     * 更新数据
     */
    @Update
    fun update(element: T) :Int
}