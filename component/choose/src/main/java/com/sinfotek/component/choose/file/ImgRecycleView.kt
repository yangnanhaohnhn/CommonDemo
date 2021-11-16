package com.sinfotek.component.choose.file

import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.widget.ImageView
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.luck.picture.lib.PictureSelector
import com.luck.picture.lib.config.PictureConfig
import com.luck.picture.lib.config.PictureMimeType
import com.luck.picture.lib.entity.LocalMedia
import com.luck.picture.lib.style.PictureParameterStyle
import com.luck.picture.lib.tools.ScreenUtils
import com.qmuiteam.qmui.widget.dialog.QMUIBottomSheet
import com.sinfotek.component.choose.R
import com.sinfotek.lib.common.*
import kotlinx.coroutines.*
import kotlinx.coroutines.Dispatchers.Main
import kotlin.coroutines.CoroutineContext

/**
 *
 * @author Y&N
 * date: 2021/11/1
 * desc:
 */
class ImgRecycleView : RecyclerView, View.OnClickListener,
    BaseQuickAdapter.OnItemClickListener,
    BaseQuickAdapter.OnItemChildClickListener, CoroutineScope {
    private var mContext: Context = context
    private val screenWidth = ScreenUtils.getScreenWidth(mContext)
    private var fileList = arrayListOf<FileModel>()
    private val footView: View
    private var imgWidth: Int = (screenWidth - ScreenUtils.dip2px(mContext, 30F)) / 3
    private val adapter: ImgRecycleViewAdapter = ImgRecycleViewAdapter(R.layout.item_img)

    constructor(context: Context) : this(context, null)
    constructor(context: Context, attrs: AttributeSet?) : this(context, attrs, 0)
    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    ) {
        itemAnimator = DefaultItemAnimator()
        layoutManager = GridLayoutManager(mContext,3)
        footView = LayoutInflater.from(context).inflate(
            R.layout.item_img, null,
        )
        adapter.onItemClickListener = this
        adapter.onItemChildClickListener = this
        initFoot()
        setAdapter(adapter)
    }

    private fun initFoot() {
        val ivImg = footView.findViewById<ImageView>(R.id.iv_img)
        ivImg.layoutParams = RelativeLayout.LayoutParams(imgWidth, imgWidth)
        ivImg.setImageResource(R.mipmap.ic_add_pic)
        ivImg.setOnClickListener(this)
        adapter.addFooterView(footView)
        //禁止footView占用一行
        adapter.isFooterViewAsFlow = true
    }

    /**
     * 设置长度
     */
    fun setWidth(dpVal: Float): ImgRecycleView {
        imgWidth = (screenWidth - ScreenUtils.dip2px(mContext, dpVal)) / 3
        val ivImg = footView.findViewById<ImageView>(R.id.iv_img)
        ivImg.layoutParams = RelativeLayout.LayoutParams(imgWidth, imgWidth)
        return this@ImgRecycleView
    }

    fun addPicture(imgPath: FileModel): ImgRecycleView {
        fileList.add(imgPath)
        return this@ImgRecycleView
    }

    fun addPicture(imgList: MutableList<FileModel>): ImgRecycleView {
        fileList.addAll(imgList)
        return this@ImgRecycleView
    }

    fun addVideo(fileModel: FileModel) {
        val item = fileModel.localMedia.realPath
        RxFileUtil.getVideoInfo(item, object : RxFileUtil.OnFinishVideoInfoListener {
            override fun onFinish(videoThumb: Bitmap?, duration: Long) {
                launch(coroutineContext) {
                    fileModel.firstImg = videoThumb
                    fileList.add(fileModel)
                    refreshAdapter()
                }
            }
        })
    }

    /**
     * 刷新
     */
    fun refreshAdapter() {
        if (ImgChooseConfig.isCanOp) {
            if (fileList.size >= ImgChooseConfig.imgCount) {
                adapter.removeAllFooterView()
            } else {
                if (adapter.footerLayoutCount == 0) {
                    adapter.setFooterView(footView)
                }
            }
        } else {
            adapter.removeAllFooterView()
        }
        adapter.setNewData(fileList)
    }

    private inner class ImgRecycleViewAdapter(layoutResId: Int) :
        BaseQuickAdapter<FileModel, BaseViewHolder>(layoutResId) {
        override fun convert(helper: BaseViewHolder?, item: FileModel?) {
            val iv = helper!!.getView<ImageView>(R.id.iv_img)
            val tvDel = helper.getView<TextView>(R.id.tv_del)
            iv.layoutParams = RelativeLayout.LayoutParams(imgWidth, imgWidth)

            when (item!!.fileType) {
                FileModel.TYPE_IMG -> {
                    helper.setGone(R.id.iv_play, false)
                    RxCoilUtil(context).load(item.localMedia.realPath).into(iv)
                }
                FileModel.TYPE_AUDIO -> {
                    helper.setGone(R.id.iv_play, true)
                    RxCoilUtil(context).load(R.mipmap.img_audio_default).into(iv)
                }
                FileModel.TYPE_VIDEO -> {
                    helper.setGone(R.id.iv_play, true)
                    if (item.firstImg == null) {
                        RxCoilUtil(context)
                            .load(R.mipmap.img_video_default)
                            .into(iv)
                    } else {
                        RxCoilUtil(context)
                            .load(item.firstImg!!)
                            .into(iv)
                    }
                }
            }

            if (ImgChooseConfig.isCanOp) {
                tvDel.visibility = View.VISIBLE
                helper.addOnClickListener(R.id.tv_del)
            } else {
                tvDel.visibility = View.GONE
            }
        }
    }

    override fun onClick(v: View?) {
        //选择对话框
        showChoosePicture { dialog, _, _, tag ->
            RxUiUtil.checkNull(ImgChooseConfig.getContext(), "Activity")
            dialog.dismiss()
            when (tag) {
                "0" -> fromPicture()
                "1" -> fromCamera()
                "2" -> fromVideo()
                else -> fromAudio()
            }
        }
    }

    private fun fromAudio() {

    }

    private fun fromVideo() {
        PictureSelector.create(ImgChooseConfig.getContext())
            .openGallery(PictureMimeType.ofVideo())
            .imageEngine(ShowImgEngine())
            .isPreviewVideo(true)
            .maxVideoSelectNum(ImgChooseConfig.imgCount - fileList.size)
            .imageSpanCount(ImgChooseConfig.spanCount)
            .videoMaxSecond(ImgChooseConfig.maxVideoSecond)
            .recordVideoSecond(ImgChooseConfig.maxVideoSecond - 1)
            .forResult(ImgChooseConfig.startVideoRequestCode)
    }

    private fun fromCamera() {
        PictureSelector.create(ImgChooseConfig.getContext())
            .openCamera(PictureMimeType.ofImage())
            .forResult(ImgChooseConfig.startCameraRequestCode)
    }

    private fun fromPicture() {
        PictureSelector.create(ImgChooseConfig.getContext())
            .openGallery(PictureMimeType.ofImage())
            .imageEngine(ShowImgEngine())
            .maxSelectNum(ImgChooseConfig.imgCount - fileList.size)
            .imageSpanCount(ImgChooseConfig.spanCount)
            .isCamera(false)
            .isCompress(false)
            .isPreviewImage(true)
            .forResult(ImgChooseConfig.startPictureRequestCode)
    }

    override fun onItemClick(adapter: BaseQuickAdapter<*, *>?, view: View?, position: Int) {
        //查看照片
        val dataList: MutableList<FileModel> = adapter!!.data as MutableList<FileModel>
        val fileModel = dataList[position]
        when (fileModel.fileType) {
            FileModel.TYPE_IMG -> externalPicturePreview(fileModel)
            FileModel.TYPE_VIDEO -> externalVideoPreview(fileModel)
        }
    }

    /**
     * 显示视频
     */
    private fun externalVideoPreview(fileModel: FileModel) {
        //查看视频
        val intent = Intent(ImgChooseConfig.getContext(), VideoExternalPreviewActivity::class.java)
        intent.putExtra(PictureConfig.EXTRA_VIDEO_PATH, fileModel.localMedia.realPath)
        intent.putExtra(PictureConfig.EXTRA_MEDIA_KEY, fileModel.localMedia)
        ImgChooseConfig.getContext().startActivity(intent)
    }

    private val tmpLocalMediaList = arrayListOf<LocalMedia>()

    /**
     * 显示照片
     */
    private fun externalPicturePreview(fileModel: FileModel) {
        tmpLocalMediaList.clear()
        tmpLocalMediaList.add(fileModel.localMedia)

        PictureSelector.create(ImgChooseConfig.getContext())
            .setPictureStyle(PictureParameterStyle())
            .imageEngine(ShowImgEngine())
            .openExternalPreview(0, tmpLocalMediaList)
    }

    override fun onItemChildClick(adapter: BaseQuickAdapter<*, *>?, view: View?, position: Int) {
        //点击删除按钮
        RxDialogUtil.showConfirmDialog(mContext, "确定要删除吗？") { _, _ ->
            fileList.removeAt(position)
            if (delListener != null) {
                delListener!!.onDelFile(position)
            }
            refreshAdapter()
        }
    }

    /**
     * 显示选择文件
     */
    private fun showChoosePicture(listener: QMUIBottomSheet.BottomListSheetBuilder.OnSheetItemClickListener) {
        val modelType = ImgChooseConfig.modelType
        val build = QMUIBottomSheet.BottomListSheetBuilder(mContext)
        build.addItem("相册选择", "0")
            .addItem("拍照", "1")
        when (modelType) {
            ImgChooseConfig.TYPE_ADD_VIDEO -> build.addItem("视频", "2")
            ImgChooseConfig.TYPE_ADD_VIDEO_AND_AUDIO -> {
                build.addItem("视频", "2")
                    .addItem("录音", "3")
            }
            else -> build.addItem("录音", "3")
        }.setOnSheetItemClickListener(listener)
            .setTitle("请选择")
            .setAddCancelBtn(true)
            .setGravityCenter(true)
            .build()
            .show()
    }

    private val job = Job()

    override val coroutineContext: CoroutineContext
        get() = Main + job

    fun close() {
        job.cancel()
    }


    var delListener: OnFileDelListener? = null

    interface OnFileDelListener {
        /**
         * 删除的position
         */
        fun onDelFile(position: Int)
    }
}

