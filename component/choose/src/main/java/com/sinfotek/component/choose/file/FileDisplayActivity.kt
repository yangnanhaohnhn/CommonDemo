package com.sinfotek.component.choose.file

import android.app.Activity
import android.content.Intent
import android.database.DatabaseUtils
import android.graphics.PixelFormat
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.text.TextUtils
import android.view.WindowManager
import android.widget.ImageView
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import com.luck.picture.lib.thread.PictureThreadUtils
import com.luck.picture.lib.tools.PictureFileUtils
import com.sinfotek.component.choose.ChooseConst
import com.sinfotek.component.choose.R
import com.sinfotek.lib.common.RxFileUtil
import com.sinfotek.lib.common.RxSdUtil
import com.tencent.smtt.sdk.TbsReaderView
import okio.BufferedSource
import okio.buffer
import okio.source
import java.io.File
import java.io.InputStream
import java.io.OutputStream
import java.net.URL
import java.util.*

/**
 *
 * @author Y&N
 * date: 2021/11/11
 * desc: 查看文件Rx
 */
class FileDisplayActivity : AppCompatActivity() {
    private var mWebView: TbsReaderView? = null
    private var url: String? = ""
    private lateinit var rlFile: RelativeLayout
    private lateinit var rlLoading: RelativeLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        window.setFormat(PixelFormat.TRANSLUCENT)
        window.setFlags(
            WindowManager.LayoutParams.FLAG_HARDWARE_ACCELERATED,
            WindowManager.LayoutParams.FLAG_HARDWARE_ACCELERATED
        )
        setContentView(R.layout.activity_file_display)
        url = intent.getStringExtra(ChooseConst.INTENT_DATA)
        if (TextUtils.isEmpty(url)) {
            finish()
        }

        findViewById<ImageView>(R.id.iv_back).setOnClickListener { finish() }
        findViewById<TextView>(R.id.tv_title).text = "文件查看"
        rlFile = findViewById(R.id.rl_file)
        rlLoading = findViewById(R.id.rl_loading)

        initData()
    }

    private fun initData() {
        if (!RxFileUtil.isHttpFile(url!!)) {
            //本地照片，判断是否是 content:
            if (RxFileUtil.isContent(url!!)) {
                val filePath = RxFileUtil.getRealPathFromUri(this, Uri.parse(url))
                if (TextUtils.isEmpty(filePath)) {
                    return
                }
                loadFile(filePath)
                return
            }
            loadFile(url!!)
            return
        }
        rlLoading.isVisible = true
        rlFile.isVisible = false
        prepareDownloadFile(url)
    }

    private fun prepareDownloadFile(url: String?) {
        PictureThreadUtils.executeBySingle(object : PictureThreadUtils.SimpleTask<String>() {
            override fun doInBackground(): String {
                //需要下载pdf,先检查本地是否有下载的
                val fileName = RxFileUtil.getFileNameIncSuffix(url!!)
                val diskCachePath = RxSdUtil.getDiskCachePath(this@FileDisplayActivity)
                val destFile = File(diskCachePath, fileName)
                return downloadFile(url, destFile)
            }

            override fun onSuccess(result: String?) {
                //显示图片
                rlLoading.isVisible = false
                rlFile.isVisible = true
                if (!TextUtils.isEmpty(result)){
                    loadFile(result!!)
                }
            }
        })
    }

    private fun downloadFile(url: String, destFile: File): String {
        val outImageUri: Uri = Uri.fromFile(destFile)
        var outputStream: OutputStream? = null
        var inputStream: InputStream? = null
        var inBuffer: BufferedSource? = null
        try {
            outputStream =
                Objects.requireNonNull(contentResolver.openOutputStream(outImageUri))
            val urlPath = URL(url)
            inputStream = urlPath.openStream()
            inBuffer = inputStream.source().buffer()
            val bufferCopy = PictureFileUtils.bufferCopy(inBuffer, outputStream)
            if (bufferCopy) {
                return PictureFileUtils.getPath(this, outImageUri)
            }
        } catch (e: Exception) {
            return ""
        } finally {
            PictureFileUtils.close(inputStream)
            PictureFileUtils.close(outputStream)
            PictureFileUtils.close(inBuffer)
        }
        return ""
    }

    /**
     * 加载文件
     */
    private fun loadFile(filePath: String) {
        mWebView = TbsReaderView(this, null)
        rlFile.addView(
            mWebView, RelativeLayout.LayoutParams(
                RelativeLayout.LayoutParams.MATCH_PARENT,
                RelativeLayout.LayoutParams.MATCH_PARENT
            )
        )
        val bundle = Bundle()
        bundle.putString(TbsReaderView.KEY_FILE_PATH, filePath)
        bundle.putString(
            TbsReaderView.KEY_TEMP_PATH, RxSdUtil.getDiskCachePath(this)
        )
        val fileName = RxFileUtil.getFileSuffix(File(filePath).name)
        val result: Boolean = mWebView!!.preOpen(fileName, false)
        if (result) {
            mWebView!!.openFile(bundle)
        }
    }

    companion object {
        fun startToActivity(activity: Activity, url: String) {
            val intent = Intent(activity, FileDisplayActivity::class.java)
            intent.putExtra(ChooseConst.INTENT_DATA, url)
            activity.startActivity(intent)
        }
    }

    override fun onDestroy() {
        if (mWebView != null) {
            mWebView!!.onStop()
        }
        super.onDestroy()
    }
}