package com.sinfotek.component.choose.file

import android.graphics.Bitmap
import android.os.Parcelable
import com.luck.picture.lib.entity.LocalMedia
import kotlinx.parcelize.Parcelize

/**
 *
 * @author Y&N
 * date: 2021/11/1
 * desc:
 */

@Parcelize
data class FileModel(
    /**
     * 0:相册/相机 2：视频 3：音频
     */
    var fileType: Int = TYPE_IMG,
    /**
     * 是否是网络照片
     */
    var isNetFile: Boolean = false,
    var firstImg: Bitmap?,
    var localMedia: LocalMedia
) : Parcelable {
    companion object {
        const val TYPE_IMG = 0
        const val TYPE_VIDEO = 1
        const val TYPE_AUDIO = 2
    }
}
