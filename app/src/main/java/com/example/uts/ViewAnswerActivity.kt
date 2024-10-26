package com.example.uts

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import com.example.uts.adapter.QuestionsAdapter
import com.example.uts.databinding.ActivityViewAnswerBinding


class ViewAnswerActivity : AppCompatActivity() {

    private lateinit var binding: ActivityViewAnswerBinding
    private lateinit var questionsAdapter: QuestionsAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        binding = ActivityViewAnswerBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Load questions and options from resources
        val questionsList = loadQuestionsFromResources()
        val optionsList = loadOptionsFromResources()

        // Initialize adapter with questions and options
        val correctAnswers = listOf(1, 2, 1)
        questionsAdapter = QuestionsAdapter(this, questionsList, optionsList, correctAnswers)
        binding.recyclerView.adapter = questionsAdapter
    }

    private fun loadQuestionsFromResources(): List<String> {
        return resources.getStringArray(R.array.questions).toList()
    }

    private fun loadOptionsFromResources(): List<Array<String>> {
        return listOf(
            resources.getStringArray(R.array.question1_options),
            resources.getStringArray(R.array.question2_options),
            resources.getStringArray(R.array.question3_options)
        )
    }
}