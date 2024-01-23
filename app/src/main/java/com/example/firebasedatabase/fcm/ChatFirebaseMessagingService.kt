package com.example.firebasedatabase.fcm

import android.Manifest
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.content.pm.PackageManager
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.example.firebasedatabase.R
import com.example.firebasedatabase.constants.NOTIFICATION_CHANNEL_ID
import com.example.firebasedatabase.constants.NOTIFICATION_ID
import com.example.firebasedatabase.constants.PERSON_INFO
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage


//class ChatFirebaseMessagingService : FirebaseMessagingService() {
//
//    override fun onMessageReceived(remoteMessage: RemoteMessage) {
//
//
//    }
//
//    override fun onNewToken(p0: String) {
//        super.onNewToken(p0)
//    }
//
//    private fun createNotificationChannel() {
//        val name = "Messenger notifications"
//        val descriptionText = "Allow messenger notification permission"
//        val importance = NotificationManager.IMPORTANCE_HIGH
//        val channel = NotificationChannel(NOTIFICATION_CHANNEL_ID, name, importance).apply {
//            description = descriptionText
//        }
//        val notificationManager =
//            getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
//        notificationManager.createNotificationChannel(channel)
//    }
//
//    private fun sendNotification() {
//        val builder = NotificationCompat.Builder(requireActivity(), NOTIFICATION_CHANNEL_ID)
//            .setContentText(binding.messageEdText.text.toString())
//            .setSmallIcon(R.drawable.messenger_icon)
//            .setContentTitle(sharedPreferences.getString(PERSON_INFO, ""))
//            .setPriority(NotificationCompat.PRIORITY_HIGH)
//
//        with(NotificationManagerCompat.from(requireActivity())) {
//            if (ActivityCompat.checkSelfPermission(
//                    requireActivity().applicationContext,
//                    Manifest.permission.POST_NOTIFICATIONS
//                ) != PackageManager.PERMISSION_GRANTED
//            ) {
//                requestPermissions(arrayOf(Manifest.permission.POST_NOTIFICATIONS), NOTIFICATION_ID)
//            } else {
//                notify(NOTIFICATION_ID, builder.build())
//            }
//        }
//    }
//}