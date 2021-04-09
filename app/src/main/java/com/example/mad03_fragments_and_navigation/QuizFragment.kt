package com.example.mad03_fragments_and_navigation

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.findNavController
import com.example.mad03_fragments_and_navigation.databinding.FragmentQuizBinding
import com.example.mad03_fragments_and_navigation.models.QuestionCatalogue
import timber.log.Timber


class QuizFragment : Fragment() {

    private lateinit var binding: FragmentQuizBinding
    private lateinit var viewModel: QuizViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_quiz, container, false)
        Timber.i("Called ViewModelProvider.get")
        viewModel = ViewModelProvider(this).get(QuizViewModel::class.java)
        binding.quizViewModel = viewModel
        binding.lifecycleOwner = viewLifecycleOwner

        viewModel.questionAnsweredCorrectly.observe(
            viewLifecycleOwner,
            Observer { questionAnsweredCorrectly ->
                if (!questionAnsweredCorrectly) {
                    sendToast("Incorrect Answer!")
                }
            })

        // navigate to QuizEndFragment
        viewModel.allQuestionsAnswered.observe(
            viewLifecycleOwner,
            Observer { allQuestionsAnswered ->
                if (allQuestionsAnswered) {
                    findNavController().navigate(
                        QuizFragmentDirections.actionQuizFragmentToQuizEndFragment(
                            "${viewModel.score.value}/${viewModel.questionsCount.value}"
                        )
                    )
                }
            })

        binding.btnNext.setOnClickListener {
            val answerId = checkSelectedAnswer()
            if (answerId != -1) {
                viewModel.nextQuestion(answerId)
            }
        }

        return binding.root
    }

    fun sendToast(text: String) {
        Toast.makeText(
            requireContext(),
            text,
            Toast.LENGTH_SHORT
        ).show()
    }

    private fun checkSelectedAnswer(): Int {

        val checkedRadioButtonId = binding.answerBox.checkedRadioButtonId
        if (checkedRadioButtonId == -1) {
            sendToast("Please select an answer!")
            return checkedRadioButtonId
        } else {
            val checkedRadioButton = binding.answerBox.findViewById<View>(checkedRadioButtonId)
            return binding.answerBox.indexOfChild(checkedRadioButton)
        }

    }

}