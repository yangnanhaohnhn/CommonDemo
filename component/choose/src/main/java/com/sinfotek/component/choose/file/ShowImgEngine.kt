package com.sinfotek.component.choose.file

import android.content.Context
import android.graphics.PointF
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import android.text.TextUtils
import android.view.View
import android.widget.ImageView
import androidx.core.graphics.drawable.RoundedBitmapDrawableFactory
import coil.target.Target
import com.luck.picture.lib.engine.ImageEngine
import com.luck.picture.lib.listener.OnImageCompleteCallback
import com.luck.picture.lib.tools.MediaUtils
import com.luck.picture.lib.widget.longimage.ImageSource
import com.luck.picture.lib.widget.longimage.ImageViewState
import com.luck.picture.lib.widget.longimage.SubsamplingScaleImageView
import com.sinfotek.lib.common.RxCoilUtil


/**
 *
 * @author Y&N
 * date: 2021/10/29
 * desc: 显示图片
 */
class ShowImgEngine : ImageEngine {
    /**
     * Loading image
     *
     * @param context
     * @param url
     * @param imageView
     */
    override fun loadImage(context: Context, url: String, imageView: ImageView) {
        if (TextUtils.isEmpty(url)) {
            return
        }
        RxCoilUtil(context).load(url).into(imageView)
    }

    /**
    加载网络图片适配长图方案
     * # 注意：此方法只有加载网络图片才会回调
     *
     * @param context
     * @param url
     * @param imageView
     */
    override fun loadImage(
        context: Context,
        url: String,
        imageView: ImageView,
        longImageView: SubsamplingScaleImageView,
        callback: OnImageCompleteCallback?
    ) {
        if (TextUtils.isEmpty(url)) {
            return
        }
        RxCoilUtil(context)
            .load(url)
            .intoLongImg(imageView, object : Target {
                override fun onStart(placeholder: Drawable?) {
                    super.onStart(placeholder)
                    callback!!.onShowLoading()
                }

                override fun onError(error: Drawable?) {
                    super.onError(error)
                    callback!!.onHideLoading()
                }

                override fun onSuccess(result: Drawable) {
                    super.onSuccess(result)
                    callback!!.onHideLoading()
                    val bitmap = result as BitmapDrawable
                    val eqLongImage =
                        MediaUtils.isLongImg(bitmap.bitmap.width, bitmap.bitmap.height)
                    longImageView.visibility = if (eqLongImage) View.VISIBLE else View.GONE
                    imageView.visibility = if (eqLongImage) View.GONE else View.VISIBLE
                    if (eqLongImage) {
                        // 加载长图
                        longImageView.isQuickScaleEnabled = true
                        longImageView.isZoomEnabled = true
                        longImageView.setDoubleTapZoomDuration(100)
                        longImageView.setMinimumScaleType(SubsamplingScaleImageView.SCALE_TYPE_CENTER_CROP)
                        longImageView.setDoubleTapZoomDpi(SubsamplingScaleImageView.ZOOM_FOCUS_CENTER)
                        longImageView.setImage(
                            ImageSource.bitmap(bitmap.bitmap),
                            ImageViewState(0F, PointF(0F, 0F), 0)
                        )
                    } else {
                        // 普通图片
                        imageView.setImageBitmap(bitmap.bitmap)
                    }
                }
            })
    }

    /**
     * Load network long graph adaption
     *
     * @param context
     * @param url
     * @param imageView
     */
    override fun loadImage(
        context: Context,
        url: String,
        imageView: ImageView,
        longImageView: SubsamplingScaleImageView
    ) {
        if (TextUtils.isEmpty(url)) {
            return
        }
        RxCoilUtil(context)
            .load(url)
            .intoLongImg(imageView, object : Target {
                override fun onSuccess(result: Drawable) {
                    super.onSuccess(result)
                    val bitmap = result as BitmapDrawable
                    val eqLongImage =
                        MediaUtils.isLongImg(bitmap.bitmap.width, bitmap.bitmap.height)
                    longImageView.visibility = if (eqLongImage) View.VISIBLE else View.GONE
                    imageView.visibility = if (eqLongImage) View.GONE else View.VISIBLE
                    if (eqLongImage) {
                        // 加载长图
                        longImageView.isQuickScaleEnabled = true
                        longImageView.isZoomEnabled = true
                        longImageView.setDoubleTapZoomDuration(100)
                        longImageView.setMinimumScaleType(SubsamplingScaleImageView.SCALE_TYPE_CENTER_CROP)
                        longImageView.setDoubleTapZoomDpi(SubsamplingScaleImageView.ZOOM_FOCUS_CENTER)
                        longImageView.setImage(
                            ImageSource.bitmap(bitmap.bitmap),
                            ImageViewState(0F, PointF(0F, 0F), 0)
                        )
                    } else {
                        // 普通图片
                        imageView.setImageBitmap(bitmap.bitmap)
                    }
                }
            })
    }

    /**
     * Load album catalog pictures
     *
     * @param context
     * @param url
     * @param imageView
     */
    override fun loadFolderImage(context: Context, url: String, imageView: ImageView) {
        if (TextUtils.isEmpty(url)) {
            return
        }
        RxCoilUtil(context)
            .load(url)
            .intoLongImg(imageView, object : Target {
                override fun onSuccess(result: Drawable) {
                    super.onSuccess(result)
                    val bitmap = result as BitmapDrawable
                    val circularBitmapDrawable =
                        RoundedBitmapDrawableFactory.create(context.resources, bitmap.bitmap)
                    circularBitmapDrawable.cornerRadius = 8f
                    imageView.setImageDrawable(circularBitmapDrawable)
                }
            })
    }

    /**
     * Load GIF image
     *
     * @param context
     * @param url
     * @param imageView
     */
    override fun loadAsGifImage(context: Context, url: String, imageView: ImageView) {
        if (TextUtils.isEmpty(url)) {
            return
        }
        RxCoilUtil(context).load(url).intoGif(imageView)
    }

    /**
     * Load picture list picture
     *
     * @param context
     * @param url
     * @param imageView
     */
    override fun loadGridImage(context: Context, url: String, imageView: ImageView) {
        if (TextUtils.isEmpty(url)) {
            return
        }
        RxCoilUtil(context)
            .load(url)
            .setScaleType(ImageView.ScaleType.CENTER_CROP)
            .into(imageView)
    }
}