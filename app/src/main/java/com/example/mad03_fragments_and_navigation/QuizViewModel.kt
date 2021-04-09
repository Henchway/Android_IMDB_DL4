package com.example.mad03_fragments_and_navigation

import android.view.View
import android.widget.RadioGroup
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.mad03_fragments_and_navigation.models.Question
import com.example.mad03_fragments_and_navigation.models.QuestionCatalogue
import timber.log.Timber

class QuizViewModel() : ViewModel() {

    val _index = MutableLiveData<Int>()
    val index: LiveData<Int>
        get() = _index

    val _questions = MutableLiveData<MutableList<Question>>()
    val questions: LiveData<MutableList<Question>>
        get() = _questions

    val _score = MutableLiveData<Int>()
    val score: LiveData<Int>
        get() = _score

    val _questionsCount = MutableLiveData<Int>()
    val questionsCount: LiveData<Int>
        get() = _questionsCount

    val _question = MutableLiveData<Question>()
    val question: LiveData<Question>
        get() = _question

    val _questionAnsweredCorrectly = MutableLiveData<Boolean>()
    val questionAnsweredCorrectly: LiveData<Boolean>
        get() = _questionAnsweredCorrectly

    val _allQuestionsAnswered = MutableLiveData<Boolean>()
    val allQuestionsAnswered: LiveData<Boolean>
        get() = _allQuestionsAnswered


    init {
        _index.value = 0
        _score.value = 0
        _allQuestionsAnswered.value = false
        _questionAnsweredCorrectly.value = true
        _questions.value = QuestionCatalogue().defaultQuestions
        _questionsCount.value = questions.value?.size
        _question.value = index.value?.let { questions.value?.get(it) }
        Timber.i("QuizViewModel created!")
    }

    override fun onCleared() {
        super.onCleared()
        Timber.i("QuizViewModel destroyed!")
    }

    fun nextQuestion(answerId: Int) {

        val selectedAnswer = question.value?.answers?.get(answerId)

        // check if is correct answer
        if (selectedAnswer?.isCorrectAnswer == true) {

            _score.value = (score.value)?.plus(1)
            _questionAnsweredCorrectly.value = true

        } else {
            _questionAnsweredCorrectly.value = false
        }

        // check if there are any questions left
        if (index.value == (questionsCount.value)?.minus(1)) {

            _allQuestionsAnswered.value = true

        } else {
            _index.value = (index.value)?.plus(1)
            // show next question
            _allQuestionsAnswered.value = false
            _question.value = index.value?.let { questions.value?.get(it) }
        }

    }
}