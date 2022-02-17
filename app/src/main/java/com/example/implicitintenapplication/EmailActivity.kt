package com.example.implicitintenapplication

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.implicitintenapplication.databinding.ActivityEmailBinding

class EmailActivity : AppCompatActivity() {

    lateinit var binding: ActivityEmailBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityEmailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnKirimEmail.setOnClickListener {
            val emailTujuan = binding.edtEmailTujuan.text.toString()
            val subject = binding.edtSubject.text.toString()
            val body = binding.edtBodyEmail.text.toString()

            if (emailTujuan.isEmpty()) {
                binding.edtEmailTujuan.error = "Email Masih kosong"
                return@setOnClickListener
            }
            if (subject.isEmpty()) {
                binding.edtSubject.error = "Subject Masih kosong"
                return@setOnClickListener
            }
            if (body.isEmpty()) {
                binding.edtBodyEmail.error = "Isi Email Masih kosong"
                return@setOnClickListener
            }

            val i = Intent(Intent.ACTION_SENDTO)
            i.data = Uri.parse("mailto:")
            i.putExtra(Intent.EXTRA_EMAIL, arrayOf(emailTujuan))
            i.putExtra(Intent.EXTRA_SUBJECT, subject)
            i.putExtra(Intent.EXTRA_TEXT, body)
            startActivity(i)
        }

    }
}