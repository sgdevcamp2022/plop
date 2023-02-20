package com.plop.plopmessenger.util

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.app.TaskStackBuilder
import android.content.Context
import android.content.Intent
import android.os.Build
import android.util.Log
import androidx.core.app.NotificationCompat
import androidx.core.net.toUri
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import com.plop.plopmessenger.R
import com.plop.plopmessenger.presentation.screen.main.MainActivity

class FirebaseCloudMessage : FirebaseMessagingService() {
    override fun onMessageReceived(message: RemoteMessage) {
        sendNotification(message.data)
    }

    override fun onNewToken(token: String) {
        Log.d("FCM","token : ${token}")
        createChannel()
    }

    private fun sendNotification(messageBody: Map<String, String>) {

        val taskDetailIntent = Intent(
            Intent.ACTION_VIEW,
            ("https://plopmessenger/" + messageBody["roomId"]).toUri(),
            this,
            MainActivity::class.java
        )

        val requestCode = System.currentTimeMillis().toInt()
        val pendingIntent: PendingIntent = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            TaskStackBuilder.create(this).run {
                addNextIntentWithParentStack(taskDetailIntent)
                getPendingIntent(requestCode, PendingIntent.FLAG_MUTABLE)
            }
        } else {
            TaskStackBuilder.create(this).run {
                addNextIntentWithParentStack(taskDetailIntent)
                getPendingIntent(requestCode, PendingIntent.FLAG_UPDATE_CURRENT)
            }
        }

        val notificationManager =
            getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        val notificationBuilder = NotificationCompat.Builder(this, "channel1")
            .setSmallIcon(R.drawable.ic_app_logo)
            .setContentTitle(messageBody["title"])
            .setContentText(messageBody["message"])
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setAutoCancel(true)
            .setContentIntent(pendingIntent)

        //알람 보내기
        notificationManager.notify(0, notificationBuilder.build())
    }
    private fun createChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel1 = NotificationChannel(
                "channel1",
                "channel1",
                NotificationManager.IMPORTANCE_HIGH)

            channel1.setShowBadge(false)
            channel1.enableVibration(true)

            val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel1)
        }

    }
}