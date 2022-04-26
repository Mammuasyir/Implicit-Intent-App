package com.example.implicitintenapplication

import android.content.Context
import android.net.wifi.WifiManager
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.implicitintenapplication.databinding.ActivityWifiBinding

class WifiActivity : AppCompatActivity() {

    lateinit var binding: ActivityWifiBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityWifiBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val wifiManager = applicationContext.getSystemService(Context.WIFI_SERVICE) as WifiManager

        if (wifiManager.isWifiEnabled){
            binding.swWifi.isChecked = true
            binding.tvStatus.text = "Wifi Aktif"

        }else{
            binding.swWifi.isChecked = false
            binding.tvStatus.text = "Wifi Belum Aktif"
        }

        binding.swWifi.setOnCheckedChangeListener { compoundButton, isChecked ->
            if (isChecked){
                wifiManager.isWifiEnabled = true
                binding.tvStatus.text = "Wifi Aktif"
            }else{
                wifiManager.isWifiEnabled = false
                binding.tvStatus.text = "Wifi Belum Aktif"
            }
        }
    }
}