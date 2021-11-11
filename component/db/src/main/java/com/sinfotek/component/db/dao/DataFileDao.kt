package com.sinfotek.component.db.dao

import androidx.room.Dao
import androidx.room.Query
import com.sinfotek.component.db.model.DataFileModel
import kotlinx.coroutines.flow.Flow

/**
 *
 * @author Y&N
 * date: 2021/11/10
 * desc:
 */
@Dao
interface DataFileDao : BaseDao<DataFileModel>{
    companion object{
        const val TAB_NAME = DataFileModel.TAB_NAME
    }


    @Query("select * from $TAB_NAME")
    fun getAllData():Flow<MutableList<DataFileModel>>

}