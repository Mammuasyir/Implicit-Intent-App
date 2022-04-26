package com.example.implicitintenapplication

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.media.MediaPlayer
import android.media.RingtoneManager
import android.os.Build
import androidx.core.app.NotificationCompat

class AlarmReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context?, intent: Intent?) {
        val mediaPlayer = MediaPlayer.create(context, R.raw.musikk)
        mediaPlayer.start()

        val teks = intent?.getStringExtra("TEKS")

        showNotification(context, teks)
    }

    private fun showNotification(context: Context?, teks: String?) {
        val idNotif = 2
        val idChannel = "notif kotlin"
        val channelName = "Kotlin Intermediet"

        val i = Intent(context, AlarmActivity::class.java)
        val pandingIntent = PendingIntent.getActivity(context,0,i,0 )

        val notificationManager = context?.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        val builder = NotificationCompat.Builder(context, idChannel)
            .setSmallIcon(R.drawable.ic_launcher_background)
            .setContentTitle("aduh")
            .setSubText("aaah")
            .setContentText("sakitt")
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION))
            .setAutoCancel(true)
            .setContentIntent(pandingIntent)





        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val notificationChannel = NotificationChannel(idChannel, channelName, NotificationManager.IMPORTANCE_HIGH)
            notificationManager.createNotificationChannel(notificationChannel)

        }

        val notifikasi = builder.build()
        notificationManager.notify(idNotif, notifikasi)
    }
}