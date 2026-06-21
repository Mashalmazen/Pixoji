package com.mashalmazen.pixoji.data.worker

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.work.CoroutineWorker
import androidx.work.ForegroundInfo
import androidx.work.WorkerParameters
import com.mashalmazen.pixoji.R
import com.mashalmazen.pixoji.presentation.MainActivity

class EmojiMosaicWorker(
    appContext: Context,
    params: WorkerParameters
) : CoroutineWorker(appContext, params) {

    private val notificationManager =
        appContext.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

    override suspend fun doWork(): Result {
        return try {
            setForeground(createForegroundInfo())

            updateNotification(50)

            updateNotification(100)

            Result.success()
        } catch (e: Exception) {
            Result.retry()
        }
    }

    private fun createForegroundInfo(): ForegroundInfo {
        val channelId = "emoji_mosaic_processing"
        val title = "Processing Image"
        val cancel = "Cancel"

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            createChannel(channelId, title)
        }

        val intent = Intent(applicationContext, MainActivity::class.java)
        val pendingIntent = PendingIntent.getActivity(
            applicationContext,
            0,
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        val notification = NotificationCompat.Builder(applicationContext, channelId)
            .setContentTitle(title)
            .setTicker(title)
            .setContentIntent(pendingIntent)
            .setSmallIcon(R.drawable.ic_notification)
            .setOngoing(true)
            .setProgress(100, 0, false)
            .build()

        return ForegroundInfo(1001, notification)
    }

    private fun updateNotification(progress: Int) {
        val channelId = "emoji_mosaic_processing"
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            createChannel(channelId, "Processing Image")
        }

        val notification = NotificationCompat.Builder(applicationContext, channelId)
            .setContentTitle("Processing Image")
            .setSmallIcon(R.drawable.ic_notification)
            .setOngoing(true)
            .setProgress(100, progress, false)
            .build()

        notificationManager.notify(1001, notification)
    }

    private fun createChannel(channelId: String, channelName: String) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                channelId,
                channelName,
                NotificationManager.IMPORTANCE_LOW
            )
            notificationManager.createNotificationChannel(channel)
        }
    }
}
