package com.example.implicitintenapplication

import android.app.AlarmManager
import android.app.PendingIntent
import android.app.TimePickerDialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.implicitintenapplication.databinding.ActivityAlarmBinding
import java.util.*

class AlarmActivity : AppCompatActivity() {

    lateinit var binding: ActivityAlarmBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding= ActivityAlarmBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnTimePicker.setOnClickListener {
            setTimeAlarm()
        }
    }

    private fun setTimeAlarm() {
        val calendar = Calendar.getInstance()
        val timePickerDialog = TimePickerDialog(this@AlarmActivity, TimePickerDialog.OnTimeSetListener { view, hourOfDay, minute ->

            val alarmManager = getSystemService(ALARM_SERVICE) as AlarmManager
            val i = Intent(this@AlarmActivity, AlarmReceiver::class.java)
            i.putExtra("TEKS", "Bangun Ayang")

            val pendingIntent = PendingIntent.getBroadcast(this@AlarmActivity,1,i,0)

            val cal = Calendar.getInstance()
            cal.set(Calendar.HOUR_OF_DAY, hourOfDay)
            cal.set(Calendar.MINUTE, minute)
            cal.set(Calendar.SECOND,0)

            alarmManager.set(AlarmManager.RTC_WAKEUP, cal.timeInMillis, pendingIntent)

            binding.tvSetAlarm.text = "Alarm diatur untuk jam ${cal.time}"

        }, calendar.get(Calendar.HOUR_OF_DAY), calendar.get(Calendar.MINUTE), true)

        timePickerDialog.show()

    }
}