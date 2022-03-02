package com.sinfotek.lib.common

import android.R.attr
import android.app.Activity
import android.content.ClipData
import android.content.Context
import android.content.res.AssetManager
import android.database.Cursor
import android.graphics.Bitmap
import android.media.MediaMetadataRetriever
import android.net.Uri
import android.provider.MediaStore
import android.text.TextUtils
import android.util.SparseArray
import java.io.*
import java.util.*
import android.content.Intent
import android.R.attr.data
import android.annotation.TargetApi
import android.content.ContentUris
import android.os.Build
import android.os.Environment
import android.provider.DocumentsContract


/**
 * Create 2021/5/21
 *
 * @author N
 * desc:
 */
object RxFileUtil {
    private const val D = "."
    private const val P = "/"
    private const val LOCAL_FILE_FLAG = "/storage"
    private const val LOCAL_FILE_HTTP_FLAG = "http"
    private const val LOCAL_FILE_HTTPS_FLAG = "https"
    private const val GIF_FLAG = "gif"

    /**
     * 删除文件
     *
     * @param file
     * @return
     */
    fun deleteFile(file: File): Boolean {
        return if (!file.exists()) {
            false
        } else file.delete()
    }

    /**
     * 读取asset 下面的数据
     *
     * @param context
     * @param destFile
     * @param assetFileName
     */
    fun readAsset(context: Context, destFile: File?, assetFileName: String?) {
        var inputStream: InputStream? = null
        var fos: FileOutputStream? = null
        try {
            inputStream = context.resources.assets.open(assetFileName!!)
            fos = FileOutputStream(destFile)
            val buf = ByteArray(1024 * 8)
            var len = 0
            while (inputStream.read(buf).also { len = it } != -1) {
                fos.write(buf, 0, len)
            }
        } catch (e: IOException) {
            e.printStackTrace()
        } finally {
            try {
                fos?.close()
            } catch (e: IOException) {
                e.printStackTrace()
            }
            try {
                inputStream?.close()
            } catch (e: IOException) {
                e.printStackTrace()
            }
        }
    }

    /**
     * 获取json文件
     *
     * @param fileName
     * @param context
     * @return
     */
    fun getJsonFile(context: Context, fileName: String?): String {
        //将json数据变成字符串
        val sb = StringBuilder()
        try {
            //获取Asset资源管理器
            val am: AssetManager = context.assets
            //通过管理器打开文件并读取
            val br = BufferedReader(InputStreamReader(am.open(fileName!!)))
            var line: String?
            while (br.readLine().also { line = it } != null) {
                sb.append(line)
            }
        } catch (e: IOException) {
            e.printStackTrace()
        }
        return sb.toString()
    }

    /**
     * 判断文件是否存在
     *
     * @param filePath
     * @return true:存在
     */
    fun isExistFile(filePath: String): Boolean {
        return if (TextUtils.isEmpty(filePath)) {
            false
        } else isExistFile(File(filePath))
    }

    /**
     * 判断文件是否存在
     *
     * @param file
     * @return
     */
    fun isExistFile(file: File): Boolean {
        return file.exists()
    }

    /**
     * 得到文件的name 不带后缀
     *
     * @param name 地址 xxxxxx.jpg
     * @return xxxxxx
     */
    fun getFileNameNoIncSuffix(name: String): String {
        if (TextUtils.isEmpty(name)) {
            return ""
        }
        return if (!name.contains(D)) {
            name
        } else name.substring(0, name.lastIndexOf(D))
    }

    /**
     * 得到文件的后缀
     *
     * @param name 地址 xxx/xx/xxxxxx.jpg
     * xxxxxx.jpg
     * @return jpg
     */
    fun getFileSuffix(name: String): String {
        if (TextUtils.isEmpty(name)) {
            return ""
        }
        return if (!name.contains(D)) {
            name
        } else name.substring(name.lastIndexOf(D) + 1)
    }

    /**
     * 得到文件的name 带后缀
     *
     * @param url 地址 xx/xxx/xxxxxx.png
     * @return xxxxxx.png
     */
    fun getFileNameIncSuffix(url: String): String {
        if (TextUtils.isEmpty(url)) {
            return ""
        }
        return if (!url.contains(P)) {
            url
        } else url.substring(url.lastIndexOf(P) + 1)
    }

    /**
     * 扫描文件
     *
     * @param file       文件夹
     * @param resList
     * @param filterName 可以为 ""/.shp
     */
    fun scanFile(file: File, resList: MutableList<String?>, filterName: String?) {
        val files = file.listFiles()
        if (files == null || files.isEmpty()) {
            return
        }
        for (item in files) {
            if (item.isDirectory) {
                scanFile(item, resList, filterName)
            } else {
                if (TextUtils.isEmpty(filterName)) {
                    resList.add(item.path)
                } else {
                    if (item.name.endsWith(filterName!!)) {
                        resList.add(item.path)
                    }
                }
            }
        }
    }

//    /**
//     * 保存Bitmap
//     *
//     * @param bitmap
//     * @return
//     */
//    @Throws(IOException::class)
//    fun saveBitmap(bitmap: Bitmap): File? {
//        var file: File? = null
//        val root = RxSdUtil.getImagePath()
//        val fileName = THUMBNAIL_NAME + "_" + System.currentTimeMillis() + ".png"
//        val fileDir = File(root!!.absolutePath + File.separator + "ArcGis Screen")
//        var isDirectoryCreated = fileDir.exists()
//        if (!isDirectoryCreated) {
//            isDirectoryCreated = fileDir.mkdirs()
//        }
//        if (isDirectoryCreated) {
//            file = File(fileDir, fileName)
//            val fos = FileOutputStream(file)
//            bitmap.compress(Bitmap.CompressFormat.PNG, 100, fos)
//            fos.flush()
//            fos.close()
//        }
//        return file
//    }

    /**
     * 保存信息到指定的文件中
     *
     * @param data
     * @param destFile
     * @param fileName
     */
    fun writeDataToFile(data: String?, destFile: File?, fileName: String?) {
        var fileName = fileName
        if (TextUtils.isEmpty(data)) {
            return
        }
        if (destFile == null) {
            return
        }
        if (!destFile.exists()) {
            return
        }
        if (TextUtils.isEmpty(fileName)) {
            val date = RxDateUtil.getDateByLong(System.currentTimeMillis(), RxDateUtil.TYPE_YMD_HMS)
            fileName = "default-$date.txt"
        }
        val file = File(destFile, fileName)
        var fw: FileWriter? = null
        try {
            fw = FileWriter(file)
            fw.write(data)
        } catch (e: IOException) {
            e.printStackTrace()
        } finally {
            try {
                fw?.close()
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    /**
     * 获取视频的一些信息
     *
     * @param path
     */
    fun getVideoInfo(path: String, listener: OnFinishVideoInfoListener?) {
        val poolProxy = RxThreadPoolUtil.instance()
        val runnable = VideoInfoRunnable(path, listener)
        poolProxy.execute(runnable)
    }

    /**
     * 获取视频文件截图
     *
     * @param filePath 视频文件的路径
     * @return Bitmap 返回获取的Bitmap
     */
    private fun getVideoThumb(filePath: String): SparseArray<Any> {
        val map: SparseArray<Any> = SparseArray<Any>(2)
        val retriever = MediaMetadataRetriever()
        try {
            //根据url获取缩略图
            if (filePath.startsWith("http://")
                || filePath.startsWith("https://")
                || filePath.startsWith("widevine://")
            ) {
//                retriever.setDataSource(filePath, new Hashtable<String, String>());
                //根据url获取缩略图
                retriever.setDataSource(filePath, HashMap())
                //获得第一帧图片
                val bitmap: Bitmap? = retriever.frameAtTime
                map.put(0, bitmap)
            } else {
                retriever.setDataSource(filePath)
                ////retriever.getFrameAtTime(-1);
                val bitmap: Bitmap? =
                    retriever.getFrameAtTime(0, MediaMetadataRetriever.OPTION_CLOSEST_SYNC)
                map.put(0, bitmap)
            }
            val durationStr: String? =
                retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_DURATION)
            if (!TextUtils.isEmpty(durationStr)) {
                val duration = durationStr!!.toLong()
                map.put(1, duration)
            }
        } catch (e: IllegalArgumentException) {
            e.printStackTrace()
            map.put(0, null)
            map.put(1, 0)
        } finally {
            retriever.release()
        }
        return map
    }

    /**
     * 是否是本地文件
     */
    fun isLocalFile(filePath: String): Boolean {
        return if (TextUtils.isEmpty(filePath))
            false
        else
            filePath.startsWith(LOCAL_FILE_FLAG)
    }

    /**
     * 是否是网络文件
     */
    fun isHttpFile(filePath: String): Boolean {
        return if (TextUtils.isEmpty(filePath))
            false
        else
            filePath.startsWith(LOCAL_FILE_HTTP_FLAG) || filePath.startsWith(LOCAL_FILE_HTTPS_FLAG)
    }

    /**
     * 是否是Gif
     */
    fun isUrlHasGif(filePath: String): Boolean {
        return if (TextUtils.isEmpty(filePath)) {
            false
        } else {
            filePath.lowercase(Locale.getDefault()).endsWith(GIF_FLAG)
        }
    }

    /**
     * 是否包含 Content
     */
    fun isContent(url: String): Boolean {
        return if (TextUtils.isEmpty(url))
            false
        else
            url.startsWith("content://")
    }

    /**
     * 是否是Video
     */
    fun isUrlHasVideo(url: String): Boolean {
        return if (TextUtils.isEmpty(url)) {
            false
        } else url.endsWith(".mp4")
    }

    /**
     * content 转 String
     */
    fun getRealPathFromUri(context: Context, contentUri: Uri): String {
        var cursor: Cursor? = null
        var result = ""
        return try {
            val data = arrayOf(MediaStore.Images.Media.DATA)
            cursor = context.contentResolver.query(contentUri, data, null, null, null)
            if (cursor != null) {
                if (cursor.moveToFirst()) {
                    result = cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.DATA))
                }
            }
            result
        } catch (e: Exception) {
            ""
        } finally {
            cursor?.close()
        }
    }


//    /**
//     * 处理图片或视频
//     *
//     * @param media
//     * @return
//     */
//    fun dealWithImgOrVideo(media: LocalMedia): String? {
//        val mediaPath: String = media.getPath()
//        if (isExistFile(mediaPath)) {
//            return mediaPath
//        }
//        val androidQToPath: String = media.getAndroidQToPath()
//        if (isExistFile(androidQToPath)) {
//            return androidQToPath
//        }
//        val realPath: String = media.getRealPath()
//        return if (isExistFile(realPath)) {
//            realPath
//        } else null
//    }

    internal class VideoInfoRunnable(
        private val path: String,
        private val listener: OnFinishVideoInfoListener?
    ) : Runnable {
        override fun run() {
            val map: SparseArray<Any> = getVideoThumb(path)
            val object1: Any = map.get(0)
            val videoThumb: Bitmap = object1 as Bitmap
            val videoDuration = map.get(1) as Long
            listener?.onFinish(videoThumb, videoDuration)
        }
    }

    interface OnFinishVideoInfoListener {
        /**
         * 完成视频的信息获取
         *
         * @param videoThumb
         * @param duration
         */
        fun onFinish(videoThumb: Bitmap?, duration: Long)
    }

    /**
     * 选择文件
     */
    fun chooseFile(
        activity: Activity,
        type: String?,
        isMulti: Boolean,
        requestCode: Int
    ) {
        val intent = Intent(Intent.ACTION_GET_CONTENT)
//        intent.setType("application/zip");//设置类型，我这里是任意类型，任意后缀的可以这样写。
        intent.type = type //设置类型，我这里是任意类型，任意后缀的可以这样写。
//        intent.type = "*/*" //设置类型，我这里是任意类型，任意后缀的可以这样写。
        //intent.setType(“image/*”);//图片
        //intent.setType(“audio/*”); //音频
        //intent.setType(“video/*”); //视频
        //intent.setType(“video/*;image/*”);//视频＋图片
        intent.addCategory(Intent.CATEGORY_OPENABLE)
        //多选参数
        intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, isMulti)
        activity.startActivityForResult(intent, requestCode)
    }

    /**
     * 选择文件
     */
    fun chooseFile(
        activity: Activity,
        typeList: MutableList<String>,
        isMulti: Boolean,
        requestCode: Int
    ) {
        val intent = Intent(Intent.ACTION_GET_CONTENT)
        var result = ""
        for (index in typeList.indices) {
            result += if (index == typeList.size - 1) {
                typeList[index]
            } else {
                typeList[index] + ";"
            }
        }
        intent.type = result
        intent.addCategory(Intent.CATEGORY_OPENABLE)
        //多选参数
        intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, isMulti)
        activity.startActivityForResult(intent, requestCode)
    }
}