package com.example.client.ui.viewmodel

import android.net.Uri
import android.util.Log
import androidx.compose.runtime.*
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.client.model.*
import com.example.client.repositories.QuizRepository
import com.example.client.repositories.TopicRepository
import com.example.client.utils.ApiResponse
import com.example.client.utils.ResourceState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import okhttp3.MultipartBody
import javax.inject.Inject

@HiltViewModel
class CreateQuizViewModel @Inject constructor(
    private val quizRepository: QuizRepository,
    private val topicRepository: TopicRepository
): ViewModel() {

    private val _types : MutableStateFlow<ResourceState<ApiResponse<List<QuestionType>>>> = MutableStateFlow(ResourceState.Nothing)
    val types = _types.asStateFlow()

    private val _topics : MutableStateFlow<ResourceState<ApiResponse<List<Topic>>>> = MutableStateFlow(ResourceState.Nothing)
    val topics = _topics.asStateFlow()

    private val _quiz : MutableStateFlow<ResourceState<ApiResponse<Quiz>>> = MutableStateFlow(ResourceState.Nothing)
    val quiz = _quiz.asStateFlow()



    private val _qImageUri = mutableStateOf<Uri?>(null)
    val qImageUri: MutableState<Uri?> = _qImageUri

    var quizThumbnailPath by mutableStateOf("")
        private set

    var questionThumbnailPath by mutableStateOf("")
        private set
    var questionTitle by mutableStateOf("")
        private set

    var questionType by mutableStateOf<QuestionType?>(null)
        private set

    var score by mutableStateOf(10)
        private set

    var timeLimit by mutableStateOf(20)
        private set


    ////
    var summary by mutableStateOf("")
        private set

    var description by mutableStateOf("")
        private set

    var title by mutableStateOf("")
        private set

    var isPublic by mutableStateOf(false)
        private set

    var topic by mutableStateOf<Topic?>(null)
        private set

    private val _imageUri = mutableStateOf<Uri?>(null)
    val imageUri: MutableState<Uri?> = _imageUri


    private var _listQuestion = MutableStateFlow<List<CreateQuestion>>(emptyList())
    val listQuestion = _listQuestion.asStateFlow()

    init {
        getAllTopic()
        getAllQuestionType()
    }



    //QUESTION
    fun onChangeQuestionType(newValue: QuestionType) { questionType = newValue }
    fun onChangeQuestionTitle(newValue: String) { questionTitle = newValue }
    fun onChangeScore(newValue: Int) { score = newValue }
    fun onChangeTimeLimit(newValue: Int) { timeLimit = newValue }
    fun onChangeQuestionImage(newValue: Uri?) {
        _qImageUri.value = newValue
    }

    fun onChangeQuestionThumbnailPath(newValue: String) {questionThumbnailPath = newValue}

    //QUIZ

    fun onChangeSummary(newValue: String) { summary = newValue }
    fun onChangeTitle(newValue: String) { title = newValue }
    fun onChangeDescription(newValue: String) { description = newValue }
    fun onChangeIsPublic(newValue: Boolean) { isPublic = newValue }
    fun onChangeTopic(newValue:Topic) {topic = newValue}
    fun onChangeImageUrl(newValue: Uri?) {
        _imageUri.value = newValue
    }

    fun onChangeQuizThumbnailPath(newValue: String) {quizThumbnailPath = newValue}

    fun deleteQuestion(index:Int){
        val updatedList = _listQuestion.value.toMutableList().apply {
            removeAt(index)
        }
        // Cập nhật StateFlow với danh sách mới
        _listQuestion.value = updatedList
    }

    private fun setAnswers():List<CreateAnswer> = arrayListOf(
        CreateAnswer(
            title = answer1,
            isCorrect = answerIndex == 0
        ),
        CreateAnswer(
            title = answer2,
            isCorrect = answerIndex == 1
        ),
        CreateAnswer(
            title = answer3,
            isCorrect = answerIndex == 2
        ),
        CreateAnswer(
            title = answer4,
            isCorrect = answerIndex == 3
        )
    )

    fun addQuestion(){
        val data = CreateQuestion(
            title = questionTitle,
            questionType = questionType!!,
            order = 0,
            timeLimit = timeLimit,
            score = score,
            answers = setAnswers(),
            thumbnail = questionThumbnailPath,
            uri = _qImageUri.value
        )
        val updatedList = _listQuestion.value.toMutableList().apply {
            add(data)
        }
        _listQuestion.value = updatedList
        resetQuestion()
        resetAnswer()
    }

    fun editQuestion(index: Int) {
        val data = CreateQuestion(
            title = questionTitle,
            questionType = questionType!!,
            order = 0,
            timeLimit = timeLimit,
            score = score,
            answers = setAnswers(),
            thumbnail = questionThumbnailPath,
            uri = _qImageUri.value
        )
        val updatedList = _listQuestion.value.toMutableList().apply {
            this[index] = data
        }
        _listQuestion.value = updatedList
    }

    fun getQuestionByIndex(index:Int): CreateQuestion {
        return _listQuestion.value[index]
    }

    fun setValueQuestionByIndex(index:Int):CreateQuestion{
        val value = _listQuestion.value[index]

        value.answers.forEachIndexed { i, createAnswer ->
            updateAnswersFields(createAnswer.title,i)
            if(createAnswer.isCorrect){
                setTrueAnswer(i)
            }
        }

        _qImageUri.value = value.uri
        questionTitle = value.title
        score = value.score
        timeLimit = value.timeLimit
        questionType = value.questionType
        questionThumbnailPath = questionThumbnailPath
        _qImageUri.value = value.uri

        return value
    }

    private var answerIndex by mutableStateOf(-1)
        private set

    private var answer1 by mutableStateOf("")
    private var answer2 by mutableStateOf("")
    private var answer3 by mutableStateOf("")
    private var answer4 by mutableStateOf("")

    fun updateAnswersFields(newValue: String, index: Int) {
        when (index) {
            0 -> { answer1 = newValue }
            1 -> { answer2 = newValue }
            2 -> { answer3 = newValue }
            else -> { answer4 = newValue }
        }
    }

    fun setAnswersFieldsValue(index: Int): String {
        return when (index) {
            0 -> { answer1 }
            1 -> { answer2 }
            2 -> { answer3 }
            else -> { answer4 }
        }
    }

    fun resetQuizState(){
        _quiz.value = ResourceState.Nothing
    }

    fun setTrueAnswer(index: Int) { answerIndex = index }

    fun resetAnswer(){
        answer1 = ""
        answer2 = ""
        answer3 = ""
        answer4 = ""
        answerIndex = -1
    }

    private fun getAllTopic(){
        viewModelScope.launch(Dispatchers.IO){
            _topics.value = ResourceState.Loading
            val response = topicRepository.getAllTopic()

            if(response is ResourceState.Success){
                _topics.value = response
            }
        }
    }

    private fun getAllQuestionType(){
        viewModelScope.launch(Dispatchers.IO){
            _types.value = ResourceState.Loading
            val response = quizRepository.getAllQuestionType()

            if(response is ResourceState.Success){
                _types.value = response
            }
        }
    }

    fun createQuiz(){
        val data = CreateQuiz(
            thumbnail = quizThumbnailPath,
            summary = summary,
            title = title,
            description = description,
            topicId = topic?.id!!,
            isPublic = isPublic.toString(),
            questions = _listQuestion.value
        )

        viewModelScope.launch (Dispatchers.IO) {
            _quiz.value = ResourceState.Loading
            val response = quizRepository.createQuiz(data)
            _quiz.value = response
        }
    }

     fun resetQuestion(){
        _qImageUri.value = null
        questionTitle = ""
        questionType = null
        score = 10
        timeLimit = 20
        questionThumbnailPath = ""
    }
}