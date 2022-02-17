package com.example.implicitintenapplication

import android.os.Bundle
import android.speech.tts.TextToSpeech
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.implicitintenapplication.databinding.ActivityTextToSpeechBinding
import java.util.*

class TextToSpeechActivity : AppCompatActivity() {

    lateinit var binding : ActivityTextToSpeechBinding
    lateinit var textToSpeech : TextToSpeech

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityTextToSpeechBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnSpeak.setOnClickListener {
            textToSpeech = TextToSpeech(this@TextToSpeechActivity, TextToSpeech.OnInitListener {
                if (it == TextToSpeech.SUCCESS){
                    val kdBahasa = textToSpeech.setLanguage(Locale("id", "ID"))
                    if (kdBahasa == TextToSpeech.LANG_MISSING_DATA || kdBahasa == TextToSpeech.LANG_NOT_SUPPORTED){
                        Toast.makeText(this, "Bahasa tidak didukung", Toast.LENGTH_SHORT).show()
                    } else{
                        val teks = binding.edtTextToSpeech.text.toString()
                        textToSpeech.speak(teks, TextToSpeech.QUEUE_FLUSH, null, null)
                    }
                }
            })
        }
    }
}