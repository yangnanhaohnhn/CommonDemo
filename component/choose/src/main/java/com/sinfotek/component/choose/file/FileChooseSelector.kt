package com.sinfotek.component.choose.file

import android.app.Activity
import com.leon.lfilepickerlibrary.LFilePicker
import com.sinfotek.component.choose.R

/**
 *
 * @author Y&N
 * date: 2021/11/15
 * desc:
 */
object FileChooseSelector {
    const val START_CHOOSE_FILE = 0x001
    const val RESULT_PATHS = "paths"
    const val RESULT_PATH = "path"
    fun chooseFile(activity: Activity) {
        chooseFile(activity, true)
    }

    fun chooseFile(activity: Activity, fileFilter: Array<String>) {
        chooseFile(activity, true, 9, fileFilter, START_CHOOSE_FILE)
    }

    fun chooseFile(activity: Activity, isMulti: Boolean) {
        chooseFile(activity, isMulti, 9, null, START_CHOOSE_FILE)
    }

    fun chooseFile(
        activity: Activity,
        isMulti: Boolean,
        maxNum: Int,
        array: Array<String>?,
        requestCode: Int
    ) {
        LFilePicker()
            .withActivity(activity)
            .withMutilyMode(isMulti)
//            .withTitle("选择文件")
            .withFileFilter(array)
            .withTitleStyle(R.style.Custom_File_Tool_Text_Style)
            .withRequestCode(requestCode)
            .withMaxNum(maxNum)
            .start()
    }
}