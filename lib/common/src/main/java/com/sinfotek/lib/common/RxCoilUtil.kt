package com.sinfotek.lib.common

import android.content.Context
import android.graphics.Bitmap
import android.net.Uri
import android.os.Build
import android.text.TextUtils
import android.widget.ImageView
import coil.*
import coil.decode.GifDecoder
import coil.decode.ImageDecoderDecoder
import coil.request.ImageRequest
import coil.target.Target
import coil.transform.Transformation
import java.io.File
import java.lang.IllegalArgumentException

/**
 *
 * @author Y&N
 * date: 2021/11/5
 * desc: 显示图片
 */
class RxCoilUtil(private val context: Context) {
    private var scaleType: ImageView.ScaleType? = ImageView.ScaleType.CENTER_CROP
    private var loadingImg = R.mipmap.ic_picture_loading
    private var errorImg = R.mipmap.ic_picture_loadfailed
    private var transformation: Transformation? = null
    private lateinit var any: Any
    private var isSetAny = false
    private var crossFadeMillis = 800

    constructor(context: Context, loadingImg: Int) : this(context) {
        this.loadingImg = loadingImg
    }

    constructor(context: Context, loadingImg: Int, errorImg: Int) : this(context, loadingImg) {
        this.errorImg = errorImg
    }


    fun load(any: Any): RxCoilUtil {
        this.any = any
        isSetAny = true
        return this
    }

    /**
     * 设置淡出的时间
     */
    fun crossFadeMillis(millis: Int): RxCoilUtil {
        this.crossFadeMillis = millis
        return this
    }

    fun setScaleType(scaleType: ImageView.ScaleType): RxCoilUtil {
        this.scaleType = scaleType
        return this
    }

    /**
     * 设置变换类型：
     * 模糊变换、圆形变换、灰度变换和圆角变换：
     */
    fun transformation(transformation: Transformation?): RxCoilUtil {
        this.transformation = transformation
        return this
    }

    /**
     * 显示图片
     */
    fun into(imageView: ImageView) {
        if (!isSetAny) {
            throw IllegalArgumentException("You must be call #load() first")
        }
        if (scaleType != null) {
            imageView.scaleType = scaleType
        }
        when (any) {
            is String -> {
                val filePath = any as String
                dealWithImg(imageView, filePath)
            }
            else -> showImg(imageView, any)
        }
    }

    /**
     * 处置文件
     * 包含 判断网络照片，本地照片
     */
    private fun dealWithImg(imageView: ImageView, filePath: String) {
        if (RxFileUtil.isHttpFile(filePath)) {
            //是网络照片
            showNetImg(imageView, filePath)
            return
        }
        var tmpRealFile = filePath
        //判断是否 content://
        if (RxFileUtil.isContent(filePath)) {
            tmpRealFile = RxFileUtil.getRealPathFromUri(context, Uri.parse(filePath))
        }
        if (TextUtils.isEmpty(tmpRealFile)) {
            showImg(imageView, tmpRealFile)
            return
        }
        if (RxFileUtil.isUrlHasGif(tmpRealFile)) {
            //显示Gif
            intoGif(imageView, tmpRealFile)
            return
        }
        if (RxFileUtil.isUrlHasVideo(tmpRealFile)) {
            //是视频
            RxFileUtil.getVideoInfo(
                tmpRealFile,
                object : RxFileUtil.OnFinishVideoInfoListener {
                    override fun onFinish(videoThumb: Bitmap?, duration: Long) {
                        if (videoThumb != null) {
                            showImg(imageView, videoThumb)
                        }
                    }
                })
            return
        }
        showLocalImg(imageView, filePath)
    }

    /**
     * 显示网络照片
     */
    private fun showNetImg(imageView: ImageView, filePath: String) {
        imageView.load(filePath) {
            // 淡入淡出
            crossfade(crossFadeMillis)
            placeholder(loadingImg)
            error(errorImg)
        }
    }


    /**
     * 显示本地照片
     */
    private fun showLocalImg(imageView: ImageView, filePath: String) {
        imageView.load(File(filePath)) {
            // 淡入淡出
            crossfade(crossFadeMillis)
            placeholder(loadingImg)
            error(errorImg)
        }
    }

    /**
     * 显示图片
     */
    private fun showImg(imageView: ImageView, any: Any) {
        imageView.loadAny(any) {
            // 淡入淡出
            crossfade(crossFadeMillis)
            placeholder(loadingImg)
            error(errorImg)
            transformation?.let { transformations(it) }
        }
    }

    /**
     * 加载Gif
     */
    fun intoGif(imageView: ImageView) {
        RxUiUtil.checkNull(imageView, "ImageView IS Not Null")
        if (!isSetAny) {
            throw IllegalArgumentException("You must be call #load() first")
        }
        val gifImageLoader = ImageLoader.Builder(context)
            .componentRegistry {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
                    add(ImageDecoderDecoder(context))
                } else {
                    add(GifDecoder())
                }
            }.build()
        imageView.loadAny(any, gifImageLoader)
    }

    /**
     * 加载Gif
     */
    fun intoGif(imageView: ImageView, filePath: String) {
        RxUiUtil.checkNull(imageView, "ImageView IS Not Null")
        if (!isSetAny) {
            throw IllegalArgumentException("You must be call #load() first")
        }
        val gifImageLoader = ImageLoader.Builder(context)
            .componentRegistry {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
                    add(ImageDecoderDecoder(context))
                } else {
                    add(GifDecoder())
                }
            }.build()
        imageView.loadAny(filePath, gifImageLoader)
    }

    /**
     * 加载长图片
     */
    fun intoLongImg(imageView: ImageView, target: Target) {
        RxUiUtil.checkNull(imageView, "ImageView IS Not Null")
        if (!isSetAny) {
            throw IllegalArgumentException("You must be call #load() first")
        }
        if (scaleType != null) {
            imageView.scaleType = scaleType
        }
        val request = ImageRequest.Builder(context)
            .data(any)
            .target(target).build()
        val enqueue = context.imageLoader.enqueue(request)
        enqueue.dispose()
    }

}