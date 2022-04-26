package com.example.implicitintenapplication

import android.app.NotificationManager
import android.content.Context
import android.content.Intent
import android.media.AudioManager
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.implicitintenapplication.databinding.ActivityAudioManagerBinding

class AudioManagerActivity : AppCompatActivity() {

    lateinit var binding: ActivityAudioManagerBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityAudioManagerBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val audioManager = getSystemService(Context.AUDIO_SERVICE) as AudioManager  //context untuk mengakses audio manager di lokasi yg diarahkan

        binding.btnRing.setOnClickListener {
            audioManager.ringerMode = AudioManager.RINGER_MODE_NORMAL
            Toast.makeText(this, "Mode Dering", Toast.LENGTH_SHORT).show()
        }

        binding.btnSilent.setOnClickListener {

            val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && !notificationManager.isNotificationPolicyAccessGranted){
                val i = Intent(Settings.ACTION_NOTIFICATION_POLICY_ACCESS_SETTINGS)
                startActivity(i)
                return@setOnClickListener
            }

            audioManager.ringerMode = AudioManager.RINGER_MODE_SILENT
            Toast.makeText(this, "Mode Silent", Toast.LENGTH_SHORT).show()
        }

        binding.btnGetar.setOnClickListener {
            audioManager.ringerMode = AudioManager.RINGER_MODE_VIBRATE
            Toast.makeText(this, "Mode Getar", Toast.LENGTH_SHORT).show()
        }


    }
}