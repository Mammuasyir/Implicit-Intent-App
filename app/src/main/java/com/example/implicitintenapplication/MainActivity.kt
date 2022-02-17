package com.example.implicitintenapplication

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.media.RingtoneManager
import android.os.Build
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.NotificationCompat
import com.example.implicitintenapplication.databinding.ActivityMainBinding
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //        3 Cara pemanggilan ID widget pada layout

//        1. FindViewById
//        var button = findViewById<Button>(R.id.btn_by_id)
//        button.setOnClickListener {
//            Toast.makeText(this,"Ini toast dari By Id", Toast.LENGTH_SHORT).show()
//        }
//
////        2. Kotlin Sintetik -> Tambahin plugin (id 'kotlin-android-extensions')
//        btn_kt_sintetik.setOnClickListener {
//            Toast.makeText(this, "Ini toast dari Kotlin sintetik", Toast.LENGTH_LONG).show()
//        }
//
////        3. View Binding
//        binding.btnViewBinding.setOnClickListener {
//            Toast.makeText(this, "Ini toast dari Binding", Toast.LENGTH_LONG).show()
//        }

        var tombolPhone = findViewById<Button>(R.id.btn_phone)
        tombolPhone.setOnClickListener {
            startActivity(Intent(this@MainActivity, PhoneActivity::class.java))
        }

        btn_sms.setOnClickListener {
            val i = Intent(this@MainActivity, SmsActivity::class.java)
            startActivity(i)
            }

        binding.btnCamera.setOnClickListener {
            val i = Intent(this@MainActivity, CameraActivity::class.java)
            startActivity(i)
        }

        binding.btnEmail
            .setOnClickListener {
            val i = Intent(this@MainActivity, EmailActivity::class.java)
            startActivity(i)
        }

        btn_text_to_speech.setOnClickListener {
            val i = Intent(this@MainActivity, TextToSpeechActivity::class.java)
            startActivity(i)
        }

        btn_notification.setOnClickListener {
            val idNotif = 1
            val idChannel = "Notif kotlin"
            val channelName = "Kotlin Intermediate"

            val i = Intent(this, SmsActivity::class.java)
            val pendingIntent = PendingIntent.getActivity(this, 0, i,0)

            val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            val builder = NotificationCompat.Builder(this, idChannel)
                .setSmallIcon(R.drawable.ic_launcher_background)
                .setContentTitle("Ingat Belajar")
                .setSubText("Belajar")
                .setContentText("Ayo belajar kotlin intermediate")
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION))
                .setAutoCancel(true)
                .setContentIntent(pendingIntent)

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                val notificationChannel = NotificationChannel(idChannel, channelName, NotificationManager.IMPORTANCE_HIGH)
                notificationManager.createNotificationChannel(notificationChannel)
        }
            val notifikasi = builder.build()
            notificationManager.notify(idNotif,notifikasi)
        }

    }
}