package com.sinfotek.component.choose.file

import android.app.Activity
import android.content.Context
import androidx.recyclerview.widget.RecyclerView
import com.luck.picture.lib.config.PictureConfig
import com.sinfotek.lib.common.const.BaseConst
import java.lang.NullPointerException
import java.lang.ref.WeakReference

/**
 *
 * @author Y&N
 * date: 2021/11/1
 * desc:
 */
class FileChooseConfig private constructor(builder: Builder) {
    init {
        spanCount = builder.spanCount
        imgCount = builder.imgCount
        isCanOp = builder.isCanOp
        maxVideoSecond = builder.maxVideoSecond
        maxAudioSecond = builder.maxAudioSecond
        mActivity = builder.mActivity
        modelType = builder.modelType
        startPictureRequestCode = builder.startPictureRequestCode
        startCameraRequestCode = builder.startCameraRequestCode
        startAudioRequestCode = builder.startAudioRequestCode
        startVideoRequestCode = builder.startVideoRequestCode
    }

    companion object {
        const val MAX_IMG_COUNT = 9
        const val TYPE_ONLY_IMG = 0
        const val TYPE_ADD_VIDEO = 1
        const val TYPE_ADD_AUDIO = 2
        const val TYPE_ADD_VIDEO_AND_AUDIO = 3
        const val START_PICTURE_REQUEST_CODE = 100

        const val START_CAMERA_REQUEST_CODE = 500
        const val START_VIDEO_REQUEST_CODE = 1000
        const val START_AUDIO_REQUEST_CODE = 1500

        var imgCount = MAX_IMG_COUNT

        var spanCount = 3

        var isCanOp = true

        var maxVideoSecond = 60

        var maxAudioSecond = 60

        var startPictureRequestCode = START_PICTURE_REQUEST_CODE

        var startCameraRequestCode = START_CAMERA_REQUEST_CODE

        var startVideoRequestCode = START_VIDEO_REQUEST_CODE

        var startAudioRequestCode = START_AUDIO_REQUEST_CODE

        private var mActivity: WeakReference<Activity>? = null

        fun getContext(): Activity =
            mActivity?.get() ?: throw NullPointerException("Activity not init")

        var modelType = TYPE_ONLY_IMG
    }

    open class Builder {
        internal var imgCount = MAX_IMG_COUNT
        fun setImgCount(imgCount: Int): Builder = apply {
            this.imgCount = imgCount
        }

        internal var isCanOp = true
        fun setIsCanOp(isCanOp: Boolean): Builder = apply {
            this.isCanOp = isCanOp
        }

        internal var spanCount = 3
        fun setSpanCount(spanCount: Int): Builder = apply {
            this.spanCount = spanCount
        }

        internal var modelType = TYPE_ONLY_IMG
        fun setModelType(modelType: Int): Builder = apply {
            this.modelType = modelType
        }

        internal var maxVideoSecond = 60
        fun setMaxVideoSecond(maxVideoSecond: Int): Builder = apply {
            this.maxVideoSecond = maxVideoSecond
        }

        internal var maxAudioSecond = 60
        fun setMaxAudioSecond(maxAudioSecond: Int): Builder = apply {
            this.maxAudioSecond = maxAudioSecond
        }

        internal var mActivity: WeakReference<Activity>? = null
        fun setActivity(mActivity: Activity): Builder = apply {
            this.mActivity = WeakReference<Activity>(mActivity)
        }

        internal var startPictureRequestCode = START_PICTURE_REQUEST_CODE
        fun setPictureRequestCode(startPictureRequestCode: Int): Builder = apply {
            this.startPictureRequestCode = startPictureRequestCode
        }

        internal var startCameraRequestCode = START_CAMERA_REQUEST_CODE
        fun setCameraRequestCode(startCameraRequestCode: Int): Builder = apply {
            this.startCameraRequestCode = startCameraRequestCode
        }

        internal var startVideoRequestCode = START_VIDEO_REQUEST_CODE
        fun setVideoRequestCode(startVideoRequestCode: Int): Builder = apply {
            this.startVideoRequestCode = startVideoRequestCode
        }

        internal var startAudioRequestCode = START_AUDIO_REQUEST_CODE
        fun setAudioRequestCode(startAudioRequestCode: Int): Builder = apply {
            this.startAudioRequestCode = startAudioRequestCode
        }

        open fun build(): FileChooseConfig = FileChooseConfig(this)
    }
}