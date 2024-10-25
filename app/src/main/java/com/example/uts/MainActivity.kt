package com.example.uts

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import com.example.uts.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.buttonStart.setOnClickListener(){
            if(binding.nameEditText.text.isNotEmpty()){
                val name =binding.nameEditText.text.toString()
                val intent = Intent(this, QuizActivity::class.java).apply {
                    putExtra("name", name)
                }
                startActivity(intent)
            } else{
                binding.nameEditText.error = "Field name harus diisi!"
            }
        }

        binding.buttonStudyMaterials.setOnClickListener(){
            val uri = "https://developer.android.com/guide/components/intents-filters"
            val intent = Intent(Intent.ACTION_VIEW)
            intent.data = Uri.parse(uri)
            startActivity(intent)
        }
    }
}