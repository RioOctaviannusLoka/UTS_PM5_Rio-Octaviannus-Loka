package com.example.uts

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import com.example.uts.databinding.ActivityResultBinding

class ResultActivity : AppCompatActivity() {
    private lateinit var binding: ActivityResultBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        binding = ActivityResultBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val name = intent.getStringExtra("name").toString()
        val Message = getString(R.string.result_message)
        binding.question.text = "$name $Message"

        binding.buttonReattempt.setOnClickListener{
            val intent = Intent(this, QuizActivity::class.java).apply {
                putExtra("name", name)
            }
            startActivity(intent)
        }

        binding.buttonViewAnswers.setOnClickListener{
            val intent = Intent(this, ViewAnswerActivity::class.java).apply {
                putExtra("name", name)
            }
            startActivity(intent)
        }

        binding.buttonStudyMaterials.setOnClickListener{
            val uri = "https://developer.android.com/guide/components/intents-filters"
            val intent = Intent(Intent.ACTION_VIEW)
            intent.data = Uri.parse(uri)
            startActivity(intent)
        }
    }
}