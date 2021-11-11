package com.sinfotek.component.db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.sinfotek.component.db.dao.DataFileDao
import com.sinfotek.component.db.model.DataFileModel

/**
 *
 * @author Y&N
 * date: 2021/11/10
 * desc:
 */
@Database(entities = [DataFileModel::class], version = DbHelper.DB_VERSION)
abstract class AbstractAppDatabase : RoomDatabase() {
   abstract fun dataFileDao(): DataFileDao
}