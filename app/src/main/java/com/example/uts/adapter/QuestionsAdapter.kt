package com.example.uts.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.uts.R
import com.example.uts.databinding.ItemQuestionBinding

class QuestionsAdapter(
    private val context: Context,
    private val questions: List<String>,
    private val options: List<Array<String>>,
    private val correctAnswers: List<Int> // Store the correct answer index for each question
) : RecyclerView.Adapter<QuestionsAdapter.QuestionViewHolder>() {

    inner class QuestionViewHolder(val binding: ItemQuestionBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(questionText: String, options: Array<String>, correctAnswerIndex: Int) {
            binding.question.text = questionText
            binding.buttonOption1.text = options[0]
            binding.buttonOption2.text = options[1]
            binding.buttonOption3.text = options[2]
            binding.buttonOption4.text = options[3]

            val correctButton = when (correctAnswerIndex) {
                0 -> binding.buttonOption1
                1 -> binding.buttonOption2
                2 -> binding.buttonOption3
                3 -> binding.buttonOption4
                else -> null
            }

            correctButton?.let {
                it.setTextAppearance(context, R.style.primaryButton_correctAnswers) // Apply style
                it.backgroundTintList =
                    context.getColorStateList(R.color.green)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): QuestionViewHolder {
        val binding = ItemQuestionBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return QuestionViewHolder(binding)
    }

    override fun onBindViewHolder(holder: QuestionViewHolder, position: Int) {
        holder.bind(questions[position], options[position], correctAnswers[position])
    }

    override fun getItemCount(): Int = questions.size
}