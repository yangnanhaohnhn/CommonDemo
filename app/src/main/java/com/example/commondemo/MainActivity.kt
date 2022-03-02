package com.example.commondemo

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.os.Bundle
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.widget.Toolbar
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.commondemo.databinding.ActivityMainBinding
import com.luck.picture.lib.PictureSelector
import com.sinfotek.component.choose.file.FileChooseSelector
import com.sinfotek.component.choose.file.FileModel
import com.sinfotek.component.choose.file.ImgChooseConfig
import com.sinfotek.lib.base.listener.CommonMvMethodImpl
import com.sinfotek.lib.base.listener.CommonMvMethodListener
import com.sinfotek.lib.base.ui.BaseMvActivity
import com.sinfotek.lib.common.RxCoilUtil
import com.sinfotek.lib.common.isEmptyList
import com.sinfotek.lib.common.log.RxLogUtil
import com.sinfotek.lib.common.permission.PermissionResult
import com.sinfotek.lib.common.permission.RxLivePermissionUtil
import com.sinfotek.module.home.HomeActivity

class MainActivity : BaseMvActivity<ActivityMainBinding, MainVm>() {
    private lateinit var toolbar: Toolbar
    override fun onBindLayoutId() = R.layout.activity_main
    override fun initView(savedInstanceState: Bundle?) {
        super.initView(savedInstanceState)

        RxLivePermissionUtil(this).requestArray(
            arrayOf(
                Manifest.permission.WRITE_EXTERNAL_STORAGE,
                Manifest.permission.READ_EXTERNAL_STORAGE,
                Manifest.permission.CAMERA
            )
        ).observe(this, {
            when (it) {
                is PermissionResult.Grant -> {
                    RxLogUtil.i("权限允许")
                }
                is PermissionResult.Rationale -> {
                    it.permissions.forEach { s ->
                        RxLogUtil.i("Rationale:${s}")
                    }
                }
                is PermissionResult.Deny -> {
                    it.permissions.forEach { s ->
                        RxLogUtil.i("deny:${s}")
                    }
                }
                else -> {

                }
            }
        })

        toolbar = findViewById(R.id.toolbar_title)
        setSupportActionBar(toolbar)
        supportActionBar!!.setHomeButtonEnabled(true)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        supportActionBar!!.setDisplayShowTitleEnabled(true)
    }

    /**
     * 设置Toolbar
     */
    override fun onPostCreate(savedInstanceState: Bundle?) {
        super.onPostCreate(savedInstanceState)
        toolbar.title = "选择"
    }

    override fun initData(savedInstanceState: Bundle?) {
        super.initData(savedInstanceState)

        //初始化选择数据
        ImgChooseConfig.Builder()
            .setActivity(this@MainActivity)
            .setImgCount(8)
            .setModelType(ImgChooseConfig.TYPE_ADD_VIDEO)
            .build()

        //1-5 都OK 显示图片、视频、长图、视频第一帧OK
        RxCoilUtil(this).load("content://media/external/file/967").into(mBinding.iv1)
        RxCoilUtil(this).load("http://bpic.588ku.com/element_origin_min_pic/16/10/29/2ac8e99273bc079e40a8dc079ca11b1f.jpg")
            .into(mBinding.iv2)
        RxCoilUtil(this).load(R.drawable.aaa).into(mBinding.iv3)
        RxCoilUtil(this).load("https://gimg2.baidu.com/image_search/src=http%3A%2F%2Fhbimg.b0.upaiyun.com%2F00ab0d85838763af900175907a51f6d1a694201a3c36c-9NyovW_fw658&refer=http%3A%2F%2Fhbimg.b0.upaiyun.com&app=2002&size=f9999,10000&q=a80&n=0&g=0n&fmt=jpeg?sec=1638692187&t=e1bda2c77e7a1f58bfa3a484fb285572")
            .into(mBinding.iv4)
        RxCoilUtil(this).load(R.mipmap.ic_launcher).into(mBinding.iv5)

        mBinding.btnStartActivity.setOnClickListener {
//            registerForActivityResult(ActivityResultContracts.StartActivityForResult()){data->
//                if (data == null){
//                    return@registerForActivityResult
//                }
//                val code = data.resultCode
//                val resCode = data.data?.getIntExtra("code",-1)
//                RxLogUtil.e("$code $resCode")
//            }.launch(Intent(this, HomeActivity::class.java))
            startActivity(Intent(this, HomeActivity::class.java))
        }
    }

    override fun onBindViewModel() = MainVm::class.java

    override fun getMvvmImpl() = object : CommonMvMethodImpl() {
        override fun onBindViewModelFactory(): ViewModelProvider.AndroidViewModelFactory {
            return AppViewModelFactory(application, this@MainActivity)
        }

        override fun onBindVariableId(): Int {
            return BR.mainVm
        }

        override fun initViewObservable() {
            super.initViewObservable()
//                mViewModel.uc.responseLiveEvent.observe(this@MainActivity, {
//                    RxLogUtil.d(it!!.toString())
//                })

            mViewModel.loginLiveData.observe(this@MainActivity, Observer {
                RxLogUtil.d(it.toString())
            })
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (Activity.RESULT_OK == resultCode) {
            if (data == null) {
                return
            }
            when (requestCode) {
                ImgChooseConfig.START_PICTURE_REQUEST_CODE -> {
                    //处置过程-视频
                    val fileList = PictureSelector.obtainMultipleResult(data)
                    RxLogUtil.e(
                        "TAG",
                        "$fileList||$requestCode"
                    )
                    if (!isEmptyList(fileList)) {
                        fileList.forEach {
                            val fileModel = FileModel(
                                FileModel.TYPE_IMG,
                                false, null, it
                            )
                            mBinding.irvFile.addPicture(fileModel)
                        }
                        mBinding.irvFile.refreshAdapter()
                    }
                }
                ImgChooseConfig.START_CAMERA_REQUEST_CODE -> {
                    //处置过程-视频
                    val fileList = PictureSelector.obtainMultipleResult(data)
                    RxLogUtil.e(
                        "TAG",
                        "$fileList||$requestCode"
                    )
                    if (!isEmptyList(fileList)) {
                        val fileModel = FileModel(
                            FileModel.TYPE_IMG,
                            false, null, fileList[0]
                        )
                        mBinding.irvFile.addPicture(fileModel)
                            .refreshAdapter()
                    }
                }
                ImgChooseConfig.START_VIDEO_REQUEST_CODE -> {
                    //处置过程-视频
                    val fileList = PictureSelector.obtainMultipleResult(data)
                    RxLogUtil.e(
                        "TAG",
                        "$fileList||$requestCode"
                    )
                    //选择视频
                    if (!isEmptyList(fileList)) {
                        val fileModel = FileModel(
                            FileModel.TYPE_VIDEO,
                            false, null, fileList[0]
                        )
                        mBinding.irvFile.addVideo(fileModel)
                    }
                }
                FileChooseSelector.START_CHOOSE_FILE -> {
                    val pathList = data.getStringArrayListExtra(FileChooseSelector.RESULT_PATHS)
                    val path = data.getStringExtra(FileChooseSelector.RESULT_PATH)
                    RxLogUtil.e(pathList!!.toString() + "|" + path)
                    //选择文件
//                    val uri = data.data
//                    if (uri != null) {
//                        val path = PictureFileUtils.getPath(this, uri)
//                        //查看文件
//                        FileDisplayActivity.startToActivity(this, path)
//                    } else {
//                        val clipData: ClipData? = data.clipData
//                        for (index in 0 until clipData!!.itemCount) {
//                            val item = clipData.getItemAt(index)
//                            RxLogUtil.e("clipData - > $item ")
//                        }
//                    }


                }
            }
        }
    }
}