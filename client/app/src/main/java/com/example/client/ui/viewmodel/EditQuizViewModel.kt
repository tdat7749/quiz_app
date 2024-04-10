package com.example.client.ui.viewmodel

import android.net.Uri
import android.util.Log
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.client.model.*
import com.example.client.repositories.QuizRepository
import com.example.client.repositories.TopicRepository
import com.example.client.utils.ApiResponse
import com.example.client.utils.ResourceState
import com.example.client.utils.Utilities
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class EditQuizViewModel @Inject constructor(
    private val quizRepository: QuizRepository,
    private val topicRepository: TopicRepository
):ViewModel() {
    private val _quiz: MutableStateFlow<ResourceState<ApiResponse<QuizDetail>>> = MutableStateFlow(ResourceState.Nothing)
    val quiz = _quiz.asStateFlow()

    private val _questions: MutableStateFlow<ResourceState<ApiResponse<List<QuestionDetail>>>> = MutableStateFlow(ResourceState.Nothing)
    val question = _questions.asStateFlow()

    private val _editQuiz: MutableStateFlow<ResourceState<ApiResponse<Quiz>>> = MutableStateFlow(ResourceState.Nothing)
    val editQuiz = _editQuiz.asStateFlow()

    private val _types : MutableStateFlow<ResourceState<ApiResponse<List<QuestionType>>>> = MutableStateFlow(ResourceState.Nothing)
    val types = _types.asStateFlow()

    private val _topics : MutableStateFlow<ResourceState<ApiResponse<List<Topic>>>> = MutableStateFlow(ResourceState.Nothing)
    val topics = _topics.asStateFlow()

    private val _changeThumbnail:MutableStateFlow<ResourceState<ApiResponse<String>>> = MutableStateFlow(ResourceState.Nothing)
    val changeThumbnail = _changeThumbnail.asStateFlow()

    private val _changeQuestionThumbnail:MutableStateFlow<ResourceState<ApiResponse<String>>> = MutableStateFlow(ResourceState.Nothing)
    val changeQuestionThumbnail = _changeQuestionThumbnail.asStateFlow()

    private val _editQuestion:MutableStateFlow<ResourceState<ApiResponse<QuestionDetail>>> = MutableStateFlow(ResourceState.Nothing)
    val editQuestion = _editQuestion.asStateFlow()

    private val _createQuestion:MutableStateFlow<ResourceState<ApiResponse<QuestionDetail>>> = MutableStateFlow(ResourceState.Nothing)
    val createQuestion = _createQuestion.asStateFlow()

    private val _deleteQuestion:MutableStateFlow<ResourceState<ApiResponse<Boolean>>> = MutableStateFlow(ResourceState.Nothing)
    val deleteQuestion = _deleteQuestion.asStateFlow()

    private val _editAnswer:MutableStateFlow<ResourceState<ApiResponse<List<Answer>>>> = MutableStateFlow(ResourceState.Nothing)
    val editAnswer = _editAnswer.asStateFlow()

    var bottomSheetThumbnail by mutableStateOf<String>("")
        private set

    var bottomSheetQuestionThumbnail by mutableStateOf<String>("")
        private set

    private val _qImageUri = mutableStateOf<Uri?>(null)
    val qImageUri: MutableState<Uri?> = _qImageUri

    var questionId by mutableStateOf(-1)
        private set

    var questionThumbnailPath by mutableStateOf("")
        private set

    var questionThumbnail by mutableStateOf<String?>("")
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

    var thumbnail by mutableStateOf<String>("")

    private val _imageUri = mutableStateOf<Uri?>(null)
    val imageUri: MutableState<Uri?> = _imageUri

    private var _listQuestion = MutableStateFlow<List<QuestionDetail>>(emptyList())
    val listQuestion = _listQuestion.asStateFlow()


    init {
        getAllTopic()
        getAllQuestionType()
    }

    fun onChangeQuestionType(newValue: QuestionType) { questionType = newValue }
    fun onChangeQuestionTitle(newValue: String) { questionTitle = newValue }
    fun onChangeScore(newValue: Int) { score = newValue }
    fun onChangeTimeLimit(newValue: Int) { timeLimit = newValue }
    fun onChangeQuestionImage(newValue: Uri?) {
        _qImageUri.value = newValue
    }


    fun onChangeQuestionThumbnailPath(newValue: String) {questionThumbnailPath = newValue}

    fun onChangeQuestionThumbnail(newValue: String) {questionThumbnail = newValue}

    //QUIZ

    fun onChangeSummary(newValue: String) { summary = newValue }
    fun onChangeTitle(newValue: String) { title = newValue }
    fun onChangeDescription(newValue: String) { description = newValue }
    fun onChangeIsPublic(newValue: Boolean) { isPublic = newValue }
    fun onChangeTopic(newValue:Topic) {topic = newValue}

    fun onChangeBottomSheetThumbnail(newValue: String) {bottomSheetThumbnail = newValue}

    fun onChangeBottomSheetQuestionThumbnail(newValue: String) {bottomSheetQuestionThumbnail = newValue}

    fun onChangeImageUrl(newValue: Uri?) {
        _imageUri.value = newValue
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

    private fun setEditAnswers():List<EditAnswer> = arrayListOf(
        EditAnswer(
            title = answer1,
            isCorrect = answerIndex == 0,
            id = idAnswer1
        ),
        EditAnswer(
            title = answer2,
            isCorrect = answerIndex == 1,
            id = idAnswer2
        ),
        EditAnswer(
            title = answer3,
            isCorrect = answerIndex == 2,
            id = idAnswer3
        ),
        EditAnswer(
            title = answer4,
            isCorrect = answerIndex == 3,
            id = idAnswer4
        )
    )

    fun editQuestion(quizId:Int,index:Int){
        val data = EditQuestion(
            title = questionTitle,
            questionTypeId = questionType!!.id,
            timeLimit = timeLimit,
            score = score,
            quizId = quizId,
            questionId = questionId
        )

        _editQuestion.value = ResourceState.Loading
        viewModelScope.launch(Dispatchers.IO) {
            val response = quizRepository.editQuestion(data)
            _editQuestion.value = response
            if(response is ResourceState.Success){
                val updatedList = _listQuestion.value.toMutableList().apply {
                    this[index] = response.value.data
                }
                _listQuestion.value = updatedList
            }
        }
    }



    fun setValueQuestionByIndex(index:Int): QuestionDetail {
        val value = _listQuestion.value[index]

        value.answers.forEachIndexed { i, answer ->
            updateAnswersFields(answer.title,i)
            updateAnswersId(answer.id,i)
            if(answer.correct){
                setTrueAnswer(i)
            }
        }

        questionTitle = value.title
        score = value.score
        timeLimit = value.timeLimit
        questionType = value.questionType
        questionThumbnail = value.thumbnail
        questionId = value.id

        return value
    }

    private var answerIndex by mutableStateOf(-1)
        private set

    private var answer1 by mutableStateOf("")
    private var answer2 by mutableStateOf("")
    private var answer3 by mutableStateOf("")
    private var answer4 by mutableStateOf("")
    private var idAnswer1 by mutableStateOf<Int>(-1)
    private var idAnswer2 by mutableStateOf<Int>(-1)
    private var idAnswer3 by mutableStateOf<Int>(-1)
    private var idAnswer4 by mutableStateOf<Int>(-1)


    fun updateAnswersFields(newValue: String, index: Int) {
        when (index) {
            0 -> {
                answer1 = newValue
            }
            1 -> {
                answer2 = newValue
            }
            2 -> {
                answer3 = newValue
            }
            else -> {
                answer4 = newValue
            }
        }
    }

    fun updateAnswersId(id: Int, index: Int) {
        when (index) {
            0 -> {
                idAnswer1 = id
            }
            1 -> {
                idAnswer2 = id
            }
            2 -> {
                idAnswer3 = id
            }
            else -> {
                idAnswer4 = id
            }
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
        idAnswer1 = -1
        idAnswer2 = -1
        idAnswer3 = -1
        idAnswer4 = -1
        answerIndex = -1
    }

    fun getQuiz(id:Int){
        _quiz.value = ResourceState.Loading
        viewModelScope.launch(Dispatchers.IO) {
            val response = quizRepository.getQuizDetail(id)
            _quiz.value = response

            if(response is ResourceState.Success){
                val quiz = response.value.data
                title = quiz.title
                description = quiz.description
                summary = quiz.summary
                isPublic = quiz.public
                topic = quiz.topic
                thumbnail = quiz.thumbnail
            }
        }
    }

    fun getQuestion(id:Int){
        _questions.value = ResourceState.Loading
        viewModelScope.launch(Dispatchers.IO) {
            val response = quizRepository.getListQuestionByQuizId(id)
            _questions.value = response

            if(response is ResourceState.Success){
                _listQuestion.value = response.value.data
            }
        }
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

    fun changeThumbnail(quizId:Int){
        val data = ChangeQuizThumbnail(
            thumbnail = bottomSheetThumbnail,
            quizId = quizId
        )

        _changeThumbnail.value = ResourceState.Loading
        viewModelScope.launch(Dispatchers.IO) {
            val response = quizRepository.changeThumbnail(data)
            _changeThumbnail.value = response

            if(response is ResourceState.Success){
                thumbnail = response.value.data
            }
        }
    }
    
    fun editQuiz(quizId: Int){
        val data = EditQuiz(
            title = title,
            slug = Utilities.Companion.slugify(title),
            summary = summary,
            description = description,
            isPublic = isPublic,
            quizId = quizId,
            topicId = topic!!.id
        )

        _editQuiz.value = ResourceState.Loading
        viewModelScope.launch(Dispatchers.IO) {
            val response = quizRepository.editQuiz(data)
            _editQuiz.value = response
        }
    }

    fun addQuestion(quizId: Int){
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
        _createQuestion.value = ResourceState.Loading
        viewModelScope.launch(Dispatchers.IO) {
            val response = quizRepository.createQuestion(data,quizId)
            _createQuestion.value = response
            if(response is ResourceState.Success){
                val updatedList = _listQuestion.value.toMutableList().apply {
                    add(response.value.data)
                }
                _listQuestion.value = updatedList
            }
        }
    }

    fun deleteQuestion(questionId:Int,quizId:Int,index:Int){
        _deleteQuestion.value =  ResourceState.Loading
        viewModelScope.launch(Dispatchers.IO) {
            val response = quizRepository.deleteQuestion(questionId,quizId)
            _deleteQuestion.value = response
            if(response is ResourceState.Success){
                val updatedList = _listQuestion.value.toMutableList().apply {
                    removeAt(index)
                }
                _listQuestion.value = updatedList
            }
        }
    }

    fun editQuestionThumbnail(questionId:Int,quizId:Int){
        val data = EditQuestionThumbnail(
            questionId = questionId,
            quizId = quizId,
            thumbnail = bottomSheetQuestionThumbnail
        )
        _changeQuestionThumbnail.value =  ResourceState.Loading
        viewModelScope.launch(Dispatchers.IO) {
            val response = quizRepository.editQuestionThumbnail(data)
            _changeQuestionThumbnail.value = response
            if(response is ResourceState.Success){
                questionThumbnail = response.value.data
            }
        }
    }

    fun editAnswer(quizId:Int,index:Int){
        val data = EditListAnswer(
            answers = setEditAnswers(),
            quizId = quizId
        )


        _editAnswer.value = ResourceState.Loading
        viewModelScope.launch(Dispatchers.IO) {
            val response = quizRepository.editListAnswer(data)
            _editAnswer.value = response

            if(response is ResourceState.Success){
                val updatedList = _listQuestion.value.toMutableList().apply {
                    var question = this[index].copy()
                    question = question.copy(answers = response.value.data)
                    this[index] = question
                }
                _listQuestion.value = updatedList
            }
        }
    }

    fun resetQuestion(){
        _qImageUri.value = null
        questionTitle = ""
        questionType = null
        score = 10
        timeLimit = 20
        questionThumbnailPath = ""
        questionThumbnail = ""
    }

    fun resetChangeThumbnailState(){
        _changeThumbnail.value = ResourceState.Nothing
        bottomSheetThumbnail = thumbnail
    }

    fun resetChangeQuestionThumbnailState(){
        _changeQuestionThumbnail.value = ResourceState.Nothing
        bottomSheetQuestionThumbnail = questionThumbnail ?: ""
    }

    fun resetEditQuizState(){
        _editQuiz.value = ResourceState.Nothing
    }

    fun resetEditQuestionState(){
        _editQuestion.value = ResourceState.Nothing
    }

    fun resetEditAnswerState(){
        _editAnswer.value = ResourceState.Nothing
    }

    fun resetCreateQuestionState(){
        _createQuestion.value = ResourceState.Nothing
    }

    fun resetDeleteeQuestionState(){
        _deleteQuestion.value = ResourceState.Nothing
    }
}