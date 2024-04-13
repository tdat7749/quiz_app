package com.example.client.model
import com.example.client.utils.AppConstants
import java.time.LocalDateTime

data class Room(
    val id:Int,
    val host:User,
    val timeStart:LocalDateTime?,
    val timeEnd:LocalDateTime?,
    val quiz:Quiz,
    val roomPin:String,
    val createdAt:String,
    val roomName: String,
    val closed:Boolean,
    val maxUser:Int,
    val playAgain:Boolean,
    val totalUser:Int,
    val mode:GameMode
)

data class RoomRealTime(
    val id:Int,
    val host:User,
    val timeStart:LocalDateTime?,
    val timeEnd:LocalDateTime?,
    val quiz:Quiz,
    val roomPin:String,
    val createdAt:String,
    val roomName: String,
    val closed:Boolean,
    val maxUser:Int,
    val totalUser:Int,
    val mode:GameMode,
    val questions:List<QuestionDetail>,
    val users: HashMap<String,User>
){
    constructor() : this(
        -1,
        User(),
        null,
        null,
        Quiz(),
        "",
        "",
        "",
        false,
        0,
        0,
        GameMode(),
        listOf(),
        hashMapOf()
    )
}


data class CreateRoom(
    val quizId:Int,
    val timeStart: LocalDateTime?,
    val timeEnd: LocalDateTime?,
    val roomName:String,
    val maxUser:Int,
    val isPlayAgain:Boolean,
    val modeId:Int
)

data class EditRoom(
    val roomId:Int,
    val timeStart:LocalDateTime?,
    val timeEnd:LocalDateTime?,
    val roomName:String,
    val isClosed:Boolean,
    val maxUser:Int,
    val isPlayAgain:Boolean
)

data class GameMode(
    val id:Int,
    val modeName:String,
    val modeCode:String
){
    constructor(): this(
        -1,
        "",
        ""
    )
}

data class JoinRoom(
    val id:Int,
    val mode:GameMode
)