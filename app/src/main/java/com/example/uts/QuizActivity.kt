package com.example.uts

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintSet
import com.example.uts.databinding.ActivityQuizBinding

class QuizActivity : AppCompatActivity() {

    private lateinit var binding: ActivityQuizBinding

    // List to store questions and options
    private lateinit var questions: Array<String>
    private lateinit var options: Array<Array<String>>

    private var currentQuestionIndex = 0
    private var correctAnswers = intArrayOf(1, 2, 1)
    private var userHasSelected = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        binding = ActivityQuizBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Load questions and options from strings.xml
        questions = resources.getStringArray(R.array.questions)
        options = arrayOf(
            resources.getStringArray(R.array.question1_options),
            resources.getStringArray(R.array.question2_options),
            resources.getStringArray(R.array.question3_options)
        )

        setupOptionListeners() // Handle Option buttons OnClick Event

        // Handle btn continue Onclick Event
        binding.buttonContinue.setOnClickListener { handleContinueClick() }

        // Change the text of question and options
        displayQuestion()
    }

    private fun  setupOptionListeners() {
        binding.buttonOption1.setOnClickListener{ onOptionSelected(0) }
        binding.buttonOption2.setOnClickListener{ onOptionSelected(1) }
        binding.buttonOption3.setOnClickListener{ onOptionSelected(2) }
        binding.buttonOption4.setOnClickListener{ onOptionSelected(3) }
    }

    private fun handleContinueClick() {
        userHasSelected = true
        currentQuestionIndex++
        if (currentQuestionIndex < questions.size) {
            displayQuestion()
        } else{
            var name = intent.getStringExtra("name").toString()
            val intent = Intent(this, ResultActivity::class.java).apply {
                putExtra("name", name)
            }
            startActivity(intent)
        }
    }

    private fun displayQuestion() {
        // Update the question and the options
        binding.question.text = questions[currentQuestionIndex]
        binding.buttonOption1.text = options[currentQuestionIndex][0]
        binding.buttonOption2.text = options[currentQuestionIndex][1]
        binding.buttonOption3.text = options[currentQuestionIndex][2]
        binding.buttonOption4.text = options[currentQuestionIndex][3]

        // Reset visibility of continue button and correct icon
        binding.buttonContinue.visibility = View.GONE
        binding.correct.visibility = View.GONE
        binding.wrong.visibility = View.GONE

        // Enable options again for the next question
        enableOptions(true)

        // Reset user selection flag
        userHasSelected = false
    }

    private fun onOptionSelected(selectedIndex: Int){
        if (userHasSelected) return // Do not allow multiple selections

        userHasSelected = true

        // Disable all buttons to prevent further selection
        enableOptions(false)

        if (selectedIndex != correctAnswers[currentQuestionIndex]) {
            // Show the wrong icon next to the selected answer
            moveIcon(binding.wrong, selectedIndex)
        }

        // Show the correct icon next to the correct answer
        moveIcon(binding.correct, correctAnswers[currentQuestionIndex])

        // Show the "Continue" button
        binding.buttonContinue.visibility = View.VISIBLE
    }

    private fun moveIcon(iconView: View, buttonIndex: Int) {
        // Find the selected option button
        val button = when (buttonIndex) {
            0 -> binding.buttonOption1
            1 -> binding.buttonOption2
            2 -> binding.buttonOption3
            3 -> binding.buttonOption4
            else -> binding.buttonOption1
        }

        // Move the icon to the left of the option button
        val set = ConstraintSet().apply {
            clone(binding.wrapper) // Clone the layout
            connect(iconView.id, ConstraintSet.END, button.id, ConstraintSet.START, 16)
            connect(iconView.id, ConstraintSet.TOP, button.id, ConstraintSet.TOP)
            connect(iconView.id, ConstraintSet.BOTTOM, button.id, ConstraintSet.BOTTOM)
        }
        set.applyTo(binding.wrapper) // Apply the new constraints
        iconView.visibility = View.VISIBLE // Show the icon
    }

    private fun enableOptions(enable: Boolean) {
        binding.buttonOption1.isEnabled = enable
        binding.buttonOption2.isEnabled = enable
        binding.buttonOption3.isEnabled = enable
        binding.buttonOption4.isEnabled = enable
    }
}