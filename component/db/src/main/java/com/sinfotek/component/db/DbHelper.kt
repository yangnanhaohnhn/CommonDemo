package com.sinfotek.component.db

import android.content.Context
import androidx.room.Room

/**
 *
 * @author Y&N
 * date: 2021/11/10
 * desc:
 */
class DbHelper private constructor(){
    private val abstractAppDatabase =
        Room.databaseBuilder(DbContext.getContext(), AbstractAppDatabase::class.java, DATABASE_NAME)
            //Room并不支持在主线程访问数据库, 除非在Builder调用allowMainThreadQueries()方法, 因为它很可能将UI锁上较长一段时间.
            .allowMainThreadQueries()
            //                .addMigrations(MIGRATION_1_2)
            .build()

    companion object {
        private var DATABASE_NAME = "demo_wim.db"
        const val DB_VERSION = 1
        @JvmStatic
        val instance: DbHelper by lazy(LazyThreadSafetyMode.SYNCHRONIZED) {
            DbHelper()
        }
    }


    fun getAbstractAppDatabase(): AbstractAppDatabase {
        return abstractAppDatabase
    }

    fun close() {
        if (abstractAppDatabase.isOpen) {
            abstractAppDatabase.close()
        }
    }
}