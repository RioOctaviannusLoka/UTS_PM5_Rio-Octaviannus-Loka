package com.example.uts

import android.content.Intent
import android.os.Bundle
import android.util.Log
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
    private var wrongIconIndex = 0

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        // Save important state to the Bundle
        outState.putInt("currentQuestionIndex", currentQuestionIndex)
        outState.putBoolean("userHasSelected", userHasSelected)
        outState.putIntArray("correctAnswers", correctAnswers)
        outState.putBoolean("isCorrectVisible", binding.correct.visibility == View.VISIBLE)
        outState.putBoolean("isWrongVisible", binding.wrong.visibility == View.VISIBLE)
        outState.putBoolean("isContinueVisible", binding.buttonContinue.visibility == View.VISIBLE)

        // Save wrong answer index
        outState.putInt("selectedIndex", if (binding.wrong.visibility == View.VISIBLE) wrongIconIndex else -1)
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)
        // Restore state from Bundle
        currentQuestionIndex = savedInstanceState.getInt("currentQuestionIndex", 0)
        userHasSelected = savedInstanceState.getBoolean("userHasSelected", false)
        correctAnswers = savedInstanceState.getIntArray("correctAnswers") ?: intArrayOf(1, 2, 1)

        Log.d("QuizActivity", "onRestoreInstanceState: currentQuestionIndex = $currentQuestionIndex")

        displayQuestion(isRestore = true)

        val correctVisible = savedInstanceState.getBoolean("isCorrectVisible", false)
        val wrongVisible = savedInstanceState.getBoolean("isWrongVisible", false)
        val selectedIndex = savedInstanceState.getInt("selectedIndex", -1)

        if (correctVisible) {
            moveIcon(binding.correct, correctAnswers[currentQuestionIndex])
        }

        if (wrongVisible && selectedIndex != -1) {
            moveIcon(binding.wrong, selectedIndex)
        }

        if (savedInstanceState.getBoolean("isContinueVisible", false)) {
            binding.buttonContinue.visibility = View.VISIBLE
        }

        if (userHasSelected) enableOptions(false)
    }

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

    private fun setupOptionListeners() {
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
            val name = intent.getStringExtra("name").toString()
            val intent = Intent(this, ResultActivity::class.java).apply {
                putExtra("name", name)
            }
            startActivity(intent)
        }
    }

    private fun displayQuestion(isRestore: Boolean = false) {
        // Update the question and options
        binding.question.text = questions[currentQuestionIndex]
        binding.buttonOption1.text = options[currentQuestionIndex][0]
        binding.buttonOption2.text = options[currentQuestionIndex][1]
        binding.buttonOption3.text = options[currentQuestionIndex][2]
        binding.buttonOption4.text = options[currentQuestionIndex][3]

        if (!isRestore) {
            // Reset only if not restoring state
            enableOptions(true)
            userHasSelected = false

            // Reset visibility of continue button and icons
            binding.buttonContinue.visibility = View.GONE
            binding.correct.visibility = View.GONE
            binding.wrong.visibility = View.GONE
        }
    }

    private fun onOptionSelected(selectedIndex: Int){
        if (userHasSelected) return // Do not allow multiple selections

        userHasSelected = true

        // Disable all buttons to prevent further selection
        enableOptions(false)

        if (selectedIndex != correctAnswers[currentQuestionIndex]) {
            wrongIconIndex = selectedIndex

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