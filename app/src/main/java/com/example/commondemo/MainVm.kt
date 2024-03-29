package com.example.commondemo

import android.app.Activity
import android.app.Application
import androidx.lifecycle.Transformations
import androidx.lifecycle.liveData
import com.luck.picture.lib.PictureSelector
import com.luck.picture.lib.config.PictureMimeType
import com.sinfotek.component.choose.file.FileChooseSelector
import com.sinfotek.component.choose.file.ShowImgEngine
import com.sinfotek.component.db.DbManager
import com.sinfotek.component.db.model.DataFileModel
import com.sinfotek.component.net.bean.ResponseResult
import com.sinfotek.lib.base.mvvm.binding.command.BindingAction
import com.sinfotek.lib.base.mvvm.binding.command.BindingActionCommand
import com.sinfotek.lib.base.mvvm.bus.SingleLiveEvent
import com.sinfotek.lib.base.mvvm.vm.BaseViewModel
import com.sinfotek.lib.common.log.RxLogUtil
import com.sinfotek.lib.common.router.startToActivity

/**
 *
 * @author Y&N
 * date: 2021/10/26
 * desc:
 */
class MainVm(activity: Activity, application: Application, model: MainModel) : BaseViewModel(application) {

    private var requestData = SingleLiveEvent<RequestParam>()

    val loginOp = BindingActionCommand(object : BindingAction {
        /**
         * 调用
         */
        override fun call() {
            requestData.value = RequestParam("adminjs", "123456")
//            showLoading("zooiajhsa")
//            launchOnIO {
//                model!!.login("adminjs", "123456", "1", object :
//                    AbstractResponseCallBack<LoginBean>("") {
//                    override fun doOnSuccess(obj: LoginBean?, msgId: String) {
//                        onSuccessResponse(obj, msgId)
//                    }
//
//                    override fun doOnComplete(msgId: String) {
//                        super.doOnComplete(msgId)
//                        hideLoading()
//                    }
//                })
//            }
        }
    })

    /**
     * 选择图片
     */
    val chooseImg = BindingActionCommand(object : BindingAction {
        override fun call() {

            PictureSelector.create(activity)
                .openGallery(PictureMimeType.ofImage())
                .maxSelectNum(3)
                .isCamera(true)
                .imageEngine(ShowImgEngine())
                .forResult(100)
        }
    })

    /**
     * 选择图片
     */
    val chooseVideo = BindingActionCommand(object : BindingAction {
        override fun call() {
            PictureSelector.create(activity)
                .openGallery(PictureMimeType.ofVideo())
                .maxVideoSelectNum(1)
                .imageEngine(ShowImgEngine())
                .isSyncCover(true)
                .isPreviewVideo(true)
                .isNotPreviewDownload(true)
                .videoMaxSecond(60)
                .recordVideoSecond(59)
                .forResult(202)
        }
    })

    val startPaging = BindingActionCommand(object : BindingAction{
        override fun call() {
            startToActivity(RouteResConst.PAGING_PATH)
        }
    })

    /**
     * 选择文件
     */
    val chooseFile = BindingActionCommand(object : BindingAction {
        override fun call() {
//            RxFileUtil.chooseFile(activity, "application/*", true, 10000)
//            FileChooseSelector.chooseFile(activity, arrayOf(".txt", ".pdf"))
            FileChooseSelector.chooseFile(activity, requestCode = 100)
        }
    })

    val insertFile = BindingActionCommand(object : BindingAction {
        /**
         * 调用
         */
        override fun call() {
            val dataFileModel = DataFileModel(
                System.currentTimeMillis().toString(),
                DataFileModel.TYPE_IMG, "xxxx"
            )
            launchOnIO {
                DbManager.instance.insertDataFileModel(dataFileModel).also { value ->
                    RxLogUtil.e(value.toString())
                }
            }
        }
    })

    val loginLiveData = Transformations.switchMap(requestData) {
        showLoading("loading....")
        liveData {
            val result = try {
                val loginBean = model.login(it!!.username, it.password, "1")
                ResponseResult.success(loginBean, "loginBean")
            } catch (e: Exception) {
                ResponseResult.error(false, e, "loginBean")
            } finally {
                hideLoading()
            }
            emit(result)
        }
    }

    inner class RequestParam(val username: String, val password: String)
}