package com.example.implicitintenapplication

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.ContactsContract
import android.telephony.SmsManager
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.example.implicitintenapplication.databinding.ActivitySmsBinding
import java.lang.Exception

class SmsActivity : AppCompatActivity() {

    lateinit var binding: ActivitySmsBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivitySmsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnPilihKontakSms.setOnClickListener {
            val i = Intent(Intent.ACTION_PICK, ContactsContract.Contacts.CONTENT_URI)
            i.data = ContactsContract.CommonDataKinds.Phone.CONTENT_URI
            startActivityForResult(i, 100)
        }

        binding.btnSmsIntent.setOnClickListener {
            val noTel = binding.edtNomorTeleponSms.text.toString()
            val msg = binding.edtPesan.text.toString()

            if (noTel.isEmpty()) {
                Toast.makeText(this@SmsActivity, "Nomor masih kosong", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            if (msg.isEmpty()) {
                Toast.makeText(this@SmsActivity, "Pesan masih kosong", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val i = Intent(Intent.ACTION_SENDTO)
            i.data = Uri.parse("sms:$noTel")
            i.putExtra("sms_body", msg)
            startActivity(i)
        }

        binding.btnKirimSms.setOnClickListener {
            val noTel = binding.edtNomorTeleponSms.text.toString()
            val msg = binding.edtPesan.text.toString()

            if (noTel.isEmpty()) {
                Toast.makeText(this@SmsActivity, "Nomor masih kosong", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            if (msg.isEmpty()) {
                Toast.makeText(this@SmsActivity, "Pesan masih kosong", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            if (ContextCompat.checkSelfPermission(
                    this@SmsActivity,
                    Manifest.permission.SEND_SMS
                ) == PackageManager.PERMISSION_GRANTED
            ) {
                val smsManager = SmsManager.getDefault()
                try {
                    smsManager.sendTextMessage(noTel, null, msg, null, null)
                    Toast.makeText(this@SmsActivity, "Berhasil kirim sms ke $noTel", Toast.LENGTH_SHORT).show()
                } catch (e: Exception) {
                    Toast.makeText(this@SmsActivity, e.localizedMessage, Toast.LENGTH_SHORT).show()
                }

            } else {
                ActivityCompat.requestPermissions(
                    this@SmsActivity,
                    arrayOf(android.Manifest.permission.SEND_SMS),
                    200
                )
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == 100) {
            if (resultCode == Activity.RESULT_OK) {
                val uri = data?.data
                val cursor = contentResolver.query(
                    uri!!,
                    arrayOf(
                        ContactsContract.CommonDataKinds.Phone.NUMBER,
                        ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME
                    ), null, null, null
                )

                if (cursor != null && cursor.moveToNext()) {
                    val noTel = cursor.getString(0)
                    binding.edtNomorTeleponSms.setText(noTel)
                }
            } else if (resultCode == Activity.RESULT_CANCELED) {
                binding.edtNomorTeleponSms.setText("")
                Toast.makeText(this, "Batal Ambil Kontak", Toast.LENGTH_SHORT).show()
            }
        }
    }
}