package com.sinfotek.component.db

import com.sinfotek.component.db.model.DataFileModel

/**
 *
 * @author Y&N
 * date: 2021/11/10
 * desc:
 */
class DbManager private constructor() {

    companion object {
        @JvmStatic
        val instance: DbManager by lazy(LazyThreadSafetyMode.SYNCHRONIZED) {
            DbManager()
        }
    }

    private val abstractAppDatabase = DbHelper.instance.getAbstractAppDatabase()
    private val dataFileDao = abstractAppDatabase.dataFileDao()

    fun insertDataFileModel(dataFileModel: DataFileModel) =
        dataFileDao.insertAll(dataFileModel)


}