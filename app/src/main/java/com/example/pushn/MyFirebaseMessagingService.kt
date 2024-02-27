package com.example.pushn

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import android.widget.RemoteViews
import androidx.core.app.NotificationCompat
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import com.google.firebase.messaging.remoteMessage

const val channelId = "notification_channel"
const val channelName ="com.example.pushn"

class MyFirebaseMessagingService : FirebaseMessagingService() {



    //generate the notification
    //attach the notification created with custom layout like notification layout
    //show th notification

    override fun onMessageReceived(remoteMessage: RemoteMessage) {
        if(remoteMessage.getNotification() != null){
            generateNotification(remoteMessage.notification!!.title!!, remoteMessage.notification!!.body!!)
        }
    }
 fun getRemoteView(title: String, message: String): RemoteViews{

    val remoteView = RemoteViews("com.example.pushn",R.layout.notification)
    remoteView.setTextViewText(R.id.title,title)
    remoteView.setTextViewText(R.id.Message,message)
     return remoteView

}


    fun generateNotification(titile: String, message: String){

        val intent = Intent(this,MainActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)

        val pendingIntent = PendingIntent.getActivity(this, 0,intent,
            PendingIntent.FLAG_ONE_SHOT or PendingIntent.FLAG_IMMUTABLE)
//channel id, channel name

        var builder : NotificationCompat.Builder = NotificationCompat.Builder(applicationContext, channelId)
            .setSmallIcon(R.drawable.noti)
            .setAutoCancel(true)
            .setVibrate(longArrayOf(1000,1000,1000,1000))
            .setOnlyAlertOnce(true)
            .setContentIntent(pendingIntent)
        builder = builder.setContent(getRemoteView(titile,message))
        
        val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val notificationChannel = NotificationChannel(channelId, channelName,NotificationManager.IMPORTANCE_HIGH)

       notificationManager.createNotificationChannel(notificationChannel)

        }
        notificationManager.notify(0,builder.build())
    }
}