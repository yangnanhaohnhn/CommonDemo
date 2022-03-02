package com.sinfotek.lib.common

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.content.pm.PackageInfo
import android.net.Uri
import java.util.*
import kotlin.math.atan2
import kotlin.math.cos
import kotlin.math.sin
import kotlin.math.sqrt

/**
 * @author Y&N
 * date: 2021/8/20
 * desc: 有关地图上面的事情，坐标转换
 */
object RxMapUtil {
    /**
     * Krasovsky 1940 (北京54)椭球长半轴
     */
    private const val A = 6378245.0

    /**
     * 椭球的偏心率
     */
    private const val EE = 0.00669342162296594323

    /**
     * 高德地图APP 包名
     */
    private const val GD_PACKAGE_NAME = "com.autonavi.minimap"

    /**
     * 百度地图APP 包名
     */
    private const val BD_PACKAGE_NAME = "com.baidu.BaiduMap"

    /**
     * 腾讯地图APP 包名
     */
    private const val TENCENT_PACKAGE_NAME = "com.tencent.map"

    /**
     * 包名是否存在
     *
     * @param mContext    实体
     * @param packageName 待检测 包名
     * @return 结果
     */
    @SuppressLint("QueryPermissionsNeeded")
    private fun haveExistPackageName(mContext: Context, packageName: String): Boolean {
        val packageInfoList: List<PackageInfo> = mContext.packageManager.getInstalledPackages(0)
        val resList: MutableList<String> = ArrayList()
        if (packageInfoList.isNotEmpty()) {
            for (packageInfo in packageInfoList) {
                resList.add(packageInfo.packageName)
            }
        }
        return resList.contains(packageName)
    }

    /**
     * 跳转高德/百度 导航功能
     *
     * @param mContext  实体
     * @param type      0:高德 1：百度 2：腾讯
     * @param lat       终点纬度信息
     * @param lgt       终点经度信息
     * @param storeName 目的地名称
     * @param mode      类型
     */
    fun openMap(
        mContext: Context,
        type: Int,
        lat: Double,
        lgt: Double,
        storeName: String,
        mode: String
    ) {
        //检测设备是否安装高德地图APP
        if (0 == type) {
            //检测设备是否安装百度地图APP
            if (!haveExistPackageName(mContext, GD_PACKAGE_NAME)) {
                showToast(mContext, "高德地图未安装!")
                return
            }
            openGdMapToGuide(mContext, lat, lgt, storeName, mode)
        } else if (1 == type) {
            //检测设备是否安装百度地图APP
            if (!haveExistPackageName(mContext, BD_PACKAGE_NAME)) {
                showToast(mContext, "百度地图未安装!")
                return
            }
            openBdMapToGuide(mContext, lat, lgt, storeName, mode)
        } else {
            //检测都未安装时，跳转网页版高德地图
            if (!haveExistPackageName(mContext, TENCENT_PACKAGE_NAME)) {
                showToast(mContext, "腾讯地图未安装!")
                return
            }
            openTencentToGuide(mContext, lat, lgt, storeName, mode)
        }
    }

    /**
     * 跳转到高德地图 并 导航到目的地
     *
     * @param mContext  实体
     * @param lat       终点纬度信息
     * @param lgt       终点经度信息
     * @param storeName 目的地名称
     * @param mode      类型 t = 0（驾车）= 1（公交）= 2（步行）= 3（骑行）= 4（火车）= 5（长途客车）
     */
    private fun openGdMapToGuide(
        mContext: Context,
        lat: Double,
        lgt: Double,
        storeName: String,
        mode: String
    ) {
        val intent = Intent(Intent.ACTION_VIEW)
        intent.setPackage(GD_PACKAGE_NAME)
        intent.addCategory(Intent.CATEGORY_DEFAULT)
        val toPoint = gps84ToGcj02(lat, lgt)
        val url = ("androidamap://route?"
                + "sourceApplication=" + R.string.app_name
                + "&sname=我的位置"
                + "&dlat=" + toPoint[0]
                + "&dlon=" + toPoint[1]
                + "&dname=" + storeName
                + "&dev=0"
                + "&m=0"
                + "&t=" + mode)
        val uri = Uri.parse(url)
        //将功能Scheme以URI的方式传入data
        intent.setData(uri)
        //启动该页面即可
        mContext.startActivity(intent)
    }

    /**
     * 打开腾讯地图（公交出行，起点位置使用地图当前位置）
     *
     * @param mContext  实体
     * @param lat       终点纬度信息
     * @param lgt       终点经度信息
     * @param storeName 目的地名称
     * @param mode      公交：type=bus，policy有以下取值
     * * 0：较快捷 、 1：少换乘 、 2：少步行 、 3：不坐地铁
     * * 驾车：type=drive，policy有以下取值
     * * 0：较快捷 、 1：无高速 、 2：距离短
     * * policy的取值缺省为0
     */
    private fun openTencentToGuide(
        mContext: Context,
        lat: Double,
        lgt: Double,
        storeName: String,
        mode: String
    ) {
        val intent = Intent(Intent.ACTION_VIEW)
        val toPoint = gps84ToGcj02(lat, lgt)
        val url = ("qqmap://map/routeplan?" +
                "type=" + mode
                + "&from=我的位置"
                + "&fromcoord=0,0"
                + "&to=" + storeName
                + "&tocoord=" + toPoint[0] + "," + toPoint[1]
                + "&policy=0"
                + "&referer=myapp")
        val uri = Uri.parse(url)
        //将功能Scheme以URI的方式传入data
        intent.data = uri
        //启动该页面即可
        mContext.startActivity(intent)
    }

    /**
     * 打开百度地图（公交出行，起点位置使用地图当前位置）
     *
     * @param mContext  实体
     * @param lat       终点纬度信息
     * @param lgt       终点经度信息
     * @param storeName 目的地名称
     * @param mode      * mode = transit（公交）、driving（驾车）、walking（步行）和riding（骑行）. 默认:driving
     * * 当 mode=transit 时 ： sy = 0：推荐路线 、 2：少换乘 、 3：少步行 、 4：不坐地铁 、 5：时间短 、 6：地铁优先
     */
    private fun openBdMapToGuide(
        mContext: Context,
        lat: Double,
        lgt: Double,
        storeName: String,
        mode: String
    ) {
        val intent = Intent(Intent.ACTION_VIEW)
        val toPoint = gps84ToBd09(lat, lgt)
        val url = ("baidumap://map/direction?"
                + "origin=我的位置"
                + "&destination=name:" + storeName + "|latlng:" + toPoint[0] + "," + toPoint[1]
                + "&mode=" + mode
                + "&sy=3"
                + "&index=0"
                + "&target=1")
        val uri = Uri.parse(url)
        //将功能Scheme以URI的方式传入data
        intent.data = uri
        //启动该页面即可
        mContext.startActivity(intent)
    }

    /**
     * 国际 GPS84 坐标系
     * 转换成
     * [国测局坐标系] 火星坐标系 (GCJ-02)
     *
     * @param lon 经度
     * @param lat 纬度
     * @return double[] 0:lat 1:lgt
     */
    fun gps84ToGcj02(lat: Double, lon: Double): DoubleArray {
        if (outOfChina(lat, lon)) {
            return doubleArrayOf(lat, lon)
        }
        var dLat = transformLat(lon - 105.0, lat - 35.0)
        var dLon = transformLon(lon - 105.0, lat - 35.0)
        val radLat = lat / 180.0 * Math.PI
        var magic = Math.sin(radLat)
        magic = 1 - EE * magic * magic
        val sqrtMagic = Math.sqrt(magic)
        dLat = dLat * 180.0 / (A * (1 - EE) / (magic * sqrtMagic) * Math.PI)
        dLon = dLon * 180.0 / (A / sqrtMagic * Math.cos(radLat) * Math.PI)
        val mgLat = lat + dLat
        val mgLon = lon + dLon
        return doubleArrayOf(mgLat, mgLon)
    }

    /**
     * [国测局坐标系] 火星坐标系 (GCJ-02)
     * 转换成
     * 国际 GPS84 坐标系
     *
     * @param lon 火星经度
     * @param lat 火星纬度
     * @return double[] 0:lat 1:lgt
     */
    fun gcj02ToGps84(lat: Double, lon: Double): DoubleArray {
        val res = transform(lat, lon)
        val resLat = lat * 2 - res[0]
        val resLon = lon * 2 - res[1]
        return doubleArrayOf(resLat, resLon)
    }

    /**
     * 火星坐标系 (GCJ-02)
     * 转换成
     * 百度坐标系 (BD-09)
     *
     * @param lat 纬度
     * @param lon 经度
     * @return double[] 0:lat 1:lg
     */
    fun gcj02ToBd09(lat: Double, lon: Double): DoubleArray {
        val z = sqrt(lon * lon + lat * lat) + 0.00002 * sin(lat * Math.PI)
        val theta = atan2(lat, lon) + 0.000003 * cos(lon * Math.PI)
        val bdLon = z * cos(theta) + 0.0065
        val bdLat = z * sin(theta) + 0.006
        return doubleArrayOf(bdLat, bdLon)
    }

    /**
     * 百度坐标系 (BD-09)
     * 转换成
     * 火星坐标系 (GCJ-02)
     *
     * @param bdLon 百度*经度
     * @param bdLat 百度*纬度
     * @return GPS实体类
     */
    fun bd09ToGcj02(bdLat: Double, bdLon: Double): DoubleArray {
        val x = bdLon - 0.0065
        val y = bdLat - 0.006
        val z = sqrt(x * x + y * y) - 0.00002 * Math.sin(y * Math.PI)
        val theta = Math.atan2(y, x) - 0.000003 * Math.cos(x * Math.PI)
        val ggLat = z * Math.sin(theta)
        val ggLon = z * Math.cos(theta)
        return doubleArrayOf(ggLat, ggLon)
    }

    /**
     * 国际 GPS84 坐标系
     * 转换成
     * 百度坐标系 (BD-09)
     *
     * @param lat:
     * @param lon:
     * @return double[] 0:lat 1:lg
     */
    fun gps84ToBd09(lat: Double, lon: Double): DoubleArray {
        val gcj02 = gps84ToGcj02(lat, lon)
        return gcj02ToBd09(gcj02[0], gcj02[1])
    }

    /**
     * 百度坐标系 (BD-09)
     * 转换成
     * 国际 GPS84 坐标系
     *
     * @param bdLon 百度*经度
     * @param bdLat 百度*纬度
     * @return GPS实体类 0:lat 1:lgt
     */
    fun bd09ToGps84(bdLat: Double, bdLon: Double): DoubleArray {
        val gcj02 = bd09ToGcj02(bdLat, bdLon)
        return gcj02ToGps84(gcj02[0], gcj02[1])
    }

    /**
     * 判断坐标是否在中国境内
     *
     * @param lat:经度
     * @param lon:纬度
     * @return :返回值 true:代表超出
     */
    fun outOfChina(lat: Double, lon: Double): Boolean {
        return lon < 72.004 || lon > 137.8347 || lat < 0.8293 || lat > 55.8271
    }

    /**
     * 转化算法
     *
     * @param lon 经度
     * @param lat 纬度
     * @return double[] 0:lat 1:lgt
     */
    private fun transform(lat: Double, lon: Double): DoubleArray {
        if (outOfChina(lat, lon)) {
            return doubleArrayOf(lat, lon)
        }
        var dLat = transformLat(lon - 105.0, lat - 35.0)
        var dLon = transformLon(lon - 105.0, lat - 35.0)
        val radLat = lat / 180.0 * Math.PI
        var magic = Math.sin(radLat)
        magic = 1 - EE * magic * magic
        val sqrtMagic = Math.sqrt(magic)
        dLat = dLat * 180.0 / (A * (1 - EE) / (magic * sqrtMagic) * Math.PI)
        dLon = dLon * 180.0 / (A / sqrtMagic * Math.cos(radLat) * Math.PI)
        val mgLat = lat + dLat
        val mgLon = lon + dLon
        return doubleArrayOf(mgLat, mgLon)
    }

    /**
     * 纬度转化算法
     *
     * @param x x坐标
     * @param y y坐标
     * @return 纬度
     */
    private fun transformLat(x: Double, y: Double): Double {
        var ret =
            -100.0 + 2.0 * x + 3.0 * y + 0.2 * y * y + 0.1 * x * y + 0.2 * Math.sqrt(Math.abs(x))
        ret += (20.0 * Math.sin(6.0 * x * Math.PI) + 20.0 * Math.sin(2.0 * x * Math.PI)) * 2.0 / 3.0
        ret += (20.0 * Math.sin(y * Math.PI) + 40.0 * Math.sin(y / 3.0 * Math.PI)) * 2.0 / 3.0
        ret += (160.0 * Math.sin(y / 12.0 * Math.PI) + 320 * Math.sin(y * Math.PI / 30.0)) * 2.0 / 3.0
        return ret
    }

    /**
     * 经度转化算法
     *
     * @param x x坐标
     * @param y y坐标
     * @return 经度
     */
    private fun transformLon(x: Double, y: Double): Double {
        var ret = 300.0 + x + 2.0 * y + 0.1 * x * x + 0.1 * x * y + 0.1 * Math.sqrt(Math.abs(x))
        ret += (20.0 * Math.sin(6.0 * x * Math.PI) + 20.0 * Math.sin(2.0 * x * Math.PI)) * 2.0 / 3.0
        ret += (20.0 * Math.sin(x * Math.PI) + 40.0 * Math.sin(x / 3.0 * Math.PI)) * 2.0 / 3.0
        ret += (150.0 * Math.sin(x / 12.0 * Math.PI) + 300.0 * Math.sin(x / 30.0 * Math.PI)) * 2.0 / 3.0
        return ret
    }
}