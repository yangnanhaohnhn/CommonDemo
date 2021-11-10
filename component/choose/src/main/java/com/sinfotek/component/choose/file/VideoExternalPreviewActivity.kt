package com.sinfotek.component.choose.file

import android.content.Context
import android.content.ContextWrapper
import android.graphics.Color
import android.media.MediaPlayer
import android.net.Uri
import android.os.Bundle
import android.text.TextUtils
import android.view.View
import android.view.WindowManager
import android.widget.*
import com.luck.picture.lib.PictureBaseActivity
import com.luck.picture.lib.config.PictureConfig
import com.luck.picture.lib.config.PictureMimeType
import com.luck.picture.lib.config.PictureSelectionConfig
import com.luck.picture.lib.entity.LocalMedia
import com.luck.picture.lib.tools.SdkVersionUtils
import com.qmuiteam.qmui.widget.dialog.QMUITipDialog
import com.sinfotek.component.choose.R
import java.util.*

/**
 *
 * @author Y&N
 * date: 2021/11/2
 * desc: 视频查看
 */
class VideoExternalPreviewActivity : PictureBaseActivity(), MediaPlayer.OnErrorListener, MediaPlayer.OnPreparedListener, MediaPlayer.OnCompletionListener, View.OnClickListener {
    private var dialog: QMUITipDialog? = null
    var videoPath: String? = null
    var ibLeftBack: ImageView? = null
    private var mMediaController: MediaController? = null
    var mVideoView: VideoView? = null
    var ivPlay: ImageView? = null
    var mPositionWhenPaused = -1

    override fun isImmersive(): Boolean {
        return false
    }

    override fun isRequestedOrientation(): Boolean {
        return false
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        window.addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
        super.onCreate(savedInstanceState)
    }

    override fun getResourceId(): Int {
        return R.layout.picture_activity_video_play
    }

    override fun initPictureSelectorStyle() {
        if (PictureSelectionConfig.style != null) {
            if (PictureSelectionConfig.style.pictureLeftBackIcon != 0) {
                ibLeftBack!!.setImageResource(PictureSelectionConfig.style.pictureLeftBackIcon)
            }
        }
    }

    override fun initWidgets() {
        super.initWidgets()
        videoPath = intent.getStringExtra(PictureConfig.EXTRA_VIDEO_PATH)
        if (TextUtils.isEmpty(videoPath)) {
            val media: LocalMedia? = intent.getParcelableExtra(PictureConfig.EXTRA_MEDIA_KEY)
            if (media == null || TextUtils.isEmpty(media.path)) {
                finish()
                return
            }
            videoPath = media.path
        }
        if (TextUtils.isEmpty(videoPath)) {
            exit()
            return
        }
        showLoading()
        ibLeftBack = findViewById(R.id.pictureLeftBack)
        mVideoView = findViewById(R.id.video_view)
        mVideoView!!.setBackgroundColor(Color.BLACK)
        ivPlay = findViewById(R.id.iv_play)
        mMediaController = MediaController(this)
        mVideoView!!.setOnCompletionListener(this)
        mVideoView!!.setOnPreparedListener(this)
        mVideoView!!.setMediaController(mMediaController)
        ibLeftBack!!.setOnClickListener(this)
        ivPlay!!.setOnClickListener(this)
    }

    /**
     * 显示加载等待弹窗
     */
    private fun showLoading() {
        if (dialog == null) {
            val builder = QMUITipDialog.Builder(this)
            builder.setIconType(QMUITipDialog.Builder.ICON_TYPE_LOADING)
            builder.setTipWord("正在加载视频...")
            dialog = builder.create(false)
        }
        dialog!!.show()
    }

    /**
     * 隐藏加载等待弹窗
     */
    private fun hideLoading() {
        if (dialog != null && dialog!!.isShowing) {
            dialog!!.dismiss()
        }
    }

    override fun onStart() {
        // Play Video
        if (SdkVersionUtils.checkedAndroid_Q() && PictureMimeType.isContent(videoPath)) {
            mVideoView!!.setVideoURI(Uri.parse(videoPath))
        } else {
            mVideoView!!.setVideoPath(videoPath)
        }
        mVideoView!!.start()
        super.onStart()
    }

    override fun onPause() {
        // Stop video when the activity is pause.
        mPositionWhenPaused = mVideoView!!.currentPosition
        mVideoView!!.stopPlayback()
        hideLoading()
        super.onPause()
    }

    override fun onDestroy() {
        mMediaController = null
        mVideoView = null
        ivPlay = null
        hideLoading()
        super.onDestroy()
    }

    override fun onResume() {
        // Resume video player
        if (mPositionWhenPaused >= 0) {
            mVideoView!!.seekTo(mPositionWhenPaused)
            mPositionWhenPaused = -1
        }
        super.onResume()
    }

    override fun onError(player: MediaPlayer?, arg1: Int, arg2: Int): Boolean {
        return false
    }

    override fun onCompletion(mp: MediaPlayer?) {
        if (null != ivPlay) {
            ivPlay!!.visibility = View.VISIBLE
        }
    }

    override fun onClick(v: View) {
        val id = v.id
        if (id == R.id.pictureLeftBack) {
            onBackPressed()
        } else if (id == R.id.iv_play) {
            mVideoView!!.start()
            ivPlay!!.visibility = View.INVISIBLE
        }
    }

    override fun onBackPressed() {
        if (PictureSelectionConfig.windowAnimationStyle != null
            && PictureSelectionConfig.windowAnimationStyle.activityPreviewExitAnimation != 0
        ) {
            finish()
            overridePendingTransition(
                0,
                PictureSelectionConfig.windowAnimationStyle.activityPreviewExitAnimation
            )
        } else {
            exit()
        }
    }

    override fun attachBaseContext(newBase: Context?) {
        super.attachBaseContext(object : ContextWrapper(newBase) {
            override fun getSystemService(name: String): Any? {
                return if (Objects.equals(AUDIO_SERVICE, name)) {
                    applicationContext.getSystemService(name)
                } else super.getSystemService(name)
            }
        })
    }

    override fun onPrepared(mp: MediaPlayer) {
        mp.setOnInfoListener { mp1: MediaPlayer?, what: Int, extra: Int ->
            if (what == MediaPlayer.MEDIA_INFO_VIDEO_RENDERING_START) {
                hideLoading()
                // video started
                mVideoView!!.setBackgroundColor(Color.TRANSPARENT)
                return@setOnInfoListener true
            }
            false
        }
    }
}
