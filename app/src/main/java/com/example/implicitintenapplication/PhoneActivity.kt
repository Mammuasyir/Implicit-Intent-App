package com.example.implicitintenapplication

import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.ContactsContract
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.example.implicitintenapplication.databinding.ActivityPhoneBinding
import kotlinx.android.synthetic.main.activity_phone.*
import java.util.jar.Manifest

class PhoneActivity : AppCompatActivity() {

    lateinit var binding: ActivityPhoneBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityPhoneBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnPilihKontak.setOnClickListener {
            val i = Intent(Intent.ACTION_PICK, ContactsContract.Contacts.CONTENT_URI)
            i.data = ContactsContract.CommonDataKinds.Phone.CONTENT_URI
            startActivityForResult(i, 100)
        }

        binding.btnDialPhone.setOnClickListener {

            val noTel = edt_no_telp.text.toString()

            if (noTel.isEmpty()) {
                edt_no_telp.error = "Nomor Masih Kosong!"
                return@setOnClickListener
            }

            val i = Intent(Intent.ACTION_DIAL)
            i.data = Uri.parse("tel:$noTel")
            startActivity(i)

        }

        binding.btnPanggil.setOnClickListener {
            val noTel = binding.edtNoTelp.text.toString()
            if (noTel.isEmpty()) {
                edt_no_telp.error = "Nomor Masih Kosong!"
                return@setOnClickListener
            }

            if (ContextCompat.checkSelfPermission(
                    this@PhoneActivity,
                    android.Manifest.permission.CALL_PHONE
                ) == PackageManager.PERMISSION_GRANTED
            ) {
                val i = Intent(Intent.ACTION_CALL)
                i.data = Uri.parse("tel:$noTel")
                startActivity(i)
            } else {
                ActivityCompat.requestPermissions(this@PhoneActivity, arrayOf(android.Manifest.permission.CALL_PHONE),200)
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

                if (cursor != null && cursor.moveToNext()){
                    val noTel = cursor.getString(0)
                    binding.edtNoTelp.setText(noTel)
                }
            }
            else if (resultCode == Activity.RESULT_CANCELED) {
                binding.edtNoTelp.setText("")
                Toast.makeText(this, "Batal Ambil Kontak", Toast.LENGTH_SHORT).show()
            }
        }
    }
}