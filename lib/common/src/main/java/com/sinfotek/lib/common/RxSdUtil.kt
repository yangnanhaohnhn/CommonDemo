package com.sinfotek.lib.common

import android.content.Context
import android.os.Environment
import java.io.File

/**
 *
 * @author Y&N
 * date: 2021/10/8
 * desc:
 */
object RxSdUtil {

    /**
     * /data
     *
     * @return
     */
    fun getDataDirectory(): File? {
        return Environment.getDataDirectory()
    }

    /**
     * /data/data/包名/files
     *
     * @return
     */
    fun getFileDir(context: Context): File? {
        return context.filesDir
    }

    /**
     * /data/data/包名/databases
     *
     * @return
     */
    fun getDbDir(context: Context, name: String?): File? {
        return context.getDatabasePath(name)
    }

    /**
     * /data/data/包名/cache
     *
     * @return
     */
    fun getFileCacheDir(context: Context): File? {
        val downloadDirectory = getDownloadDirectory()
        if (!downloadDirectory.exists()) {
            return null
        }
        val file = File(downloadDirectory, context.packageName)
        if (!file.exists()) {
            file.mkdirs()
        }
        val file1 = File(file, "crash")
        if (!file1.exists()) {
            file1.mkdirs()
        }
        return file1
    }

    /**
     * /storage/sdcard/ 或者是 /storage/emulated/0/
     *
     * @return
     */
    fun getExternalStorageDirectory(): File? {
        return Environment.getExternalStorageDirectory()
    }


    /**
     * /storage/sdcard/ 或者是 /storage/emulated/0/download
     *
     * @return
     */
    fun getDownloadDirectory(): File {
        return Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)
    }


    /**
     * /storage/sdcard/ 或者是 /storage/emulated/0/pictures
     *
     * @return
     */
    fun getImagePath(): File? {
        return Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES)
    }

    /**
     * SD 卡
     *
     * @return
     */
    fun isExistSD(): Boolean {
        return Environment.getExternalStorageState() == Environment.MEDIA_MOUNTED
    }
}