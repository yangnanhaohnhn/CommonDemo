package com.sinfotek.lib.common.router

import android.os.Bundle
import androidx.fragment.app.FragmentActivity
import com.alibaba.android.arouter.core.LogisticsCenter
import com.alibaba.android.arouter.launcher.ARouter
import com.sinfotek.lib.common.ActivityManage
import com.sinfotek.lib.common.const.BaseConst

/**
 * Create 2021/5/10
 *
 * @author N
 * desc:
 */
object RxRouteUtil {
    /**
     * 退出指定的Activity
     * @param path
     */
    fun finishActivity(path: String?) {
//        val postcard = ARouter.getInstance().build(path)
//        LogisticsCenter.completion(postcard)
//        val tClass: Class<*> = postcard.destination
//        ActivityManage.instance.finishActivity(tClass)
    }

    /**
     * 跳转
     *
     * @param path
     */
    fun startToActivity(path: String?) {
        ARouter.getInstance().build(path).withTransition(0, 0).navigation()
    }

    /**
     * 跳转
     *
     * @param path
     * @param flag
     */
    fun startToActivity(path: String?, flag: Int) {
        ARouter.getInstance().build(path)
            .withTransition(0, 0)
            .addFlags(flag)
            .navigation()
    }

    /**
     * 跳转带参数
     *
     * @param path
     * @param bundle
     */
    fun startToActivity(path: String?, bundle: Bundle?) {
        ARouter.getInstance().build(path)
            .withBundle(BaseConst.INTENT_DATA, bundle)
            .withTransition(0, 0)
            .navigation()
    }

    /**
     * 跳转带参数
     *
     * @param path
     * @param bundle
     * @param flag
     */
    fun startToActivity(path: String?, bundle: Bundle?, flag: Int) {
        ARouter.getInstance().build(path)
            .withBundle(BaseConst.INTENT_DATA, bundle)
            .withTransition(0, 0)
            .addFlags(flag)
            .navigation()
    }

    /**
     * 跳转页面
     *
     * @param activity
     * @param path
     * @param requestCode
     */
    fun startForResult(activity: FragmentActivity?, path: String?, requestCode: Int) {
        ARouter.getInstance().build(path)
            .withTransition(0, 0)
            .navigation(activity, requestCode)
    }

    /**
     * 跳转页面
     *
     * @param activity
     * @param path
     * @param requestCode
     */
    fun startForResult(activity: FragmentActivity?, path: String?, bundle: Bundle?, requestCode: Int) {
        ARouter.getInstance().build(path)
            .withBundle(BaseConst.INTENT_DATA, bundle)
            .withTransition(0, 0)
            .navigation(activity, requestCode)
    } //    /**
    //     * 跳转页面
    //     *
    //     * @param clz    所跳转的目的Activity类
    //     * @param bundle 跳转所携带的信息
    //     */
    //    public static void startToActivity(Context context, Class<?> clz, Bundle bundle) {
    //        Intent intent = new Intent(context, clz);
    //        if (bundle != null) {
    //            intent.putExtras(bundle);
    //        }
    //        context.startActivity(intent);
    //    }
    //
    //    /**
    //     * 跳转页面
    //     *
    //     * @param clz 所跳转的目的Activity类
    //     */
    //    public static void startToActivity(Context context, Class<?> clz) {
    //        startToActivity(context, clz, null);
    //    }
    //
    //    /**
    //     * 跳转页面
    //     *
    //     * @param clz 所跳转的目的Activity类
    //     */
    //    public static void startForResult(Activity activity, Class<?> clz, int requestCode) {
    //        startForResult(activity, clz, null, requestCode);
    //    }
    //
    //    /**
    //     * 跳转页面
    //     *
    //     * @param clz 所跳转的目的Activity类
    //     */
    //    public static void startForResult(Activity activity, Class<?> clz, Bundle bundle, int requestCode) {
    //        Intent intent = new Intent(activity, clz);
    //        if (bundle != null) {
    //            intent.putExtras(bundle);
    //        }
    //        activity.startActivityForResult(intent, requestCode);
    //    }
}