package com.example.client.model

import java.time.LocalDate
import java.time.LocalDateTime
import java.util.Date

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
    val onwer:Boolean?
)


data class CreateRoom(
    val quizId:Int,
    val timeStart: LocalDateTime?,
    val timeEnd: LocalDateTime?,
    val roomName:String,
    val maxUser:Int,
    val playAgain:Boolean
)

data class EditRoom(
    val roomId:Int,
    val timeStart:LocalDateTime?,
    val timeEnd:LocalDateTime?,
    val roomName:String,
    val isClosed:Boolean,
    val maxUser:Int,
    val playAgain:Boolean
)