package com.sinfotek.lib.common

import android.app.Notification
import androidx.annotation.DrawableRes
import android.os.Build
import androidx.core.app.NotificationCompat
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Intent
import androidx.annotation.RequiresApi
import android.app.NotificationChannel
import android.content.Context

/**
 * Create 2021/6/16
 *
 * @author N
 * desc:
 */
object RxNotifyUtil {
    /**
     * 显示开始下载是的通知
     *
     * @param notifyId
     * @param channelId
     * @param channelName
     * @param icon
     * @param title
     * @param content
     */
    fun showStartNotification(
        context: Context,
        notifyId: Int,
        channelId: String?,
        channelName: String?,
        @DrawableRes icon: Int,
        title: CharSequence,
        content: CharSequence,
        isVibrate: Boolean,
        isSound: Boolean
    ) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            createNotificationChannel(context, channelId, channelName, isVibrate, isSound)
        }
        val builder = buildNotification(context, channelId, icon, title, content)
        builder.priority = NotificationManager.IMPORTANCE_HIGH
        if (isVibrate && isSound) {
            builder.setDefaults(Notification.DEFAULT_VIBRATE or Notification.DEFAULT_SOUND)
        } else if (isVibrate) {
            builder.setDefaults(Notification.DEFAULT_VIBRATE)
        } else if (isSound) {
            builder.setDefaults(Notification.DEFAULT_SOUND)
        }
        val notification = builder.build()
        notification.flags = Notification.FLAG_NO_CLEAR or Notification.FLAG_ONLY_ALERT_ONCE
        notifyNotification(context, notifyId, notification)
    }

    /**
     * 显示下载中的通知（更新进度）
     *
     * @param notifyId
     * @param channelId
     * @param icon
     * @param title
     * @param content
     * @param progress
     * @param size
     */
    fun showProgressNotification(
        context: Context,
        notifyId: Int,
        channelId: String?,
        @DrawableRes icon: Int,
        title: CharSequence,
        content: CharSequence,
        progress: Int,
        size: Int
    ) {
        val builder =
            buildNotification(context, channelId, icon, title, content, progress, size)
        val notification = builder.build()
        notification.flags = Notification.FLAG_NO_CLEAR or Notification.FLAG_ONLY_ALERT_ONCE
        notifyNotification(context, notifyId, notification)
    }

    /**
     * 显示下载完成时的通知（点击安装）
     *
     * @param notifyId
     * @param channelId
     * @param icon
     * @param title
     * @param content
     */
    fun showFinishNotification(
        context: Context,
        notifyId: Int,
        channelId: String?,
        @DrawableRes icon: Int,
        title: CharSequence,
        content: CharSequence
    ) {
        cancelNotification(context, notifyId)
        val builder = buildNotification(context, channelId, icon, title, content)
        builder.setAutoCancel(true)
        //        Intent intent = AppUtils.getInstallIntent(context,file,authority);
//        PendingIntent clickIntent = PendingIntent.getActivity(context, notifyId,intent, PendingIntent.FLAG_UPDATE_CURRENT);
//        builder.setContentIntent(clickIntent);
        val notification = builder.build()
        notification.flags = Notification.FLAG_AUTO_CANCEL
        notifyNotification(context, notifyId, notification)
    }

    const val KEY_RE_DOWNLOAD = "app_update_re_download"
    const val KEY_UPDATE_CONFIG = "app_update_config"

    /**
     * 现在下载失败通知
     *
     * @param context
     * @param notifyId
     * @param channelId
     * @param icon
     * @param title
     * @param content
     * @param isReDownload
     */
    fun showErrorNotification(
        context: Context,
        notifyId: Int,
        channelId: String?,
        @DrawableRes icon: Int,
        title: CharSequence,
        content: CharSequence,
        isReDownload: Boolean
    ) {
        cancelNotification(context, notifyId)
        val builder = buildNotification(context, channelId, icon, title, content)
        builder.setAutoCancel(true)
        if (isReDownload) { //重新下载
//            Intent intent = new Intent(context, DownloadService.class);
//            intent.putExtra(KEY_RE_DOWNLOAD, true);
//            intent.putExtra(KEY_UPDATE_CONFIG, data.getTaskId());
//            PendingIntent clickIntent = PendingIntent.getService(context, notifyId, intent, PendingIntent.FLAG_UPDATE_CURRENT);
//            builder.setContentIntent(clickIntent);
        } else {
            val clickIntent = PendingIntent.getService(
                context,
                notifyId,
                Intent(),
                PendingIntent.FLAG_UPDATE_CURRENT
            )
            builder.setContentIntent(clickIntent)
        }
        val notification = builder.build()
        notification.flags = Notification.FLAG_AUTO_CANCEL
        notifyNotification(context, notifyId, notification)
    }

    /**
     * 显示通知信息（非第一次）
     *
     * @param notifyId
     * @param channelId
     * @param icon
     * @param title
     * @param content
     */
    fun showNotification(
        context: Context,
        notifyId: Int,
        channelId: String?,
        @DrawableRes icon: Int,
        title: CharSequence,
        content: CharSequence,
        isAutoCancel: Boolean
    ) {
        val builder = buildNotification(context, channelId, icon, title, content)
        builder.setAutoCancel(isAutoCancel)
        val notification = builder.build()
        notification.flags = Notification.FLAG_AUTO_CANCEL
        notifyNotification(context, notifyId, notification)
    }

    /**
     * 取消通知
     *
     * @param notifyId
     */
    fun cancelNotification(context: Context, notifyId: Int) {
        getNotificationManager(context).cancel(notifyId)
    }

    /**
     * 获取通知管理器
     *
     * @return
     */
    private fun getNotificationManager(context: Context): NotificationManager {
        return context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
    }

    /**
     * 创建一个通知渠道（兼容0以上版本）
     *
     * @param channelId
     * @param channelName
     */
    @RequiresApi(api = Build.VERSION_CODES.O)
    fun createNotificationChannel(
        context: Context,
        channelId: String?,
        channelName: String?,
        isVibrate: Boolean,
        isSound: Boolean
    ) {
        val channel =
            NotificationChannel(channelId, channelName, NotificationManager.IMPORTANCE_HIGH)
        channel.enableVibration(isVibrate)
        if (!isSound) {
            channel.setSound(null, null)
        }
        getNotificationManager(context).createNotificationChannel(channel)
    }
    /**
     * 构建一个通知构建器
     *
     * @param channelId
     * @param icon
     * @param title
     * @param content
     * @param progress
     * @param size
     * @return
     */
    /**
     * 构建一个通知构建器
     *
     * @param channelId
     * @param icon
     * @param title
     * @param content
     * @return
     */
    private fun buildNotification(
        context: Context,
        channelId: String?,
        @DrawableRes icon: Int,
        title: CharSequence,
        content: CharSequence,
        progress: Int = -1,
        size: Int = -1
    ): NotificationCompat.Builder {
        val builder = NotificationCompat.Builder(context, channelId!!)
        builder.setSmallIcon(icon)
        builder.setContentTitle(title)
        builder.setContentText(content)
        builder.setOngoing(true)
        if (progress != -1 && size != -1) {
            builder.setProgress(size, progress, false)
        }

//        Intent intent = new Intent(context, OfflineDownloadManagerActivity.class);
//        PendingIntent pi = PendingIntent.getActivities(context, 0, new Intent[]{intent}, PendingIntent.FLAG_CANCEL_CURRENT);
//        builder.setContentIntent(pi);
        return builder
    }

    /**
     * 更新通知栏
     *
     * @param id
     * @param notification
     */
    private fun notifyNotification(context: Context, id: Int, notification: Notification) {
        getNotificationManager(context).notify(id, notification)
    }
}