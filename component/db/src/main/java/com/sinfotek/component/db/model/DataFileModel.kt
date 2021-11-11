package com.sinfotek.component.db.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.io.Serializable

/**
 *
 * @author Y&N
 * date: 2021/11/10
 * desc:
 */
@Entity(tableName = DataFileModel.TAB_NAME)
class DataFileModel(
    @ColumnInfo val dataFileSign: String,

    /**
     * 文件的类型
     * 0：图片 1：视频 2：录音
     */
    @ColumnInfo val fileType: Int,

    @ColumnInfo val filePath: String
) : Serializable {

    /**
     * 数据库表的id
     */
    @PrimaryKey(autoGenerate = true)
    var dataFileId: Long = 0

    /**
     * 文件的其他信息
     */
    @ColumnInfo
    var fileOtherInfo: String = ""

    /**
     * 绑定的外键
     */
    @ColumnInfo
    var foreignKey: String = ""

    companion object {
        const val TAB_NAME = "tb_data_file"
        const val TYPE_IMG = 0
        const val TYPE_VIDEO = 1
        const val TYPE_AUDIO = 2
    }
}
