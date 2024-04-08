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
    val closed:Boolean
)


data class CreateRoom(
    val quizId:Int,
    val timeStart: LocalDateTime?,
    val timeEnd: LocalDateTime?,
    val roomName:String
)

data class EditRoom(
    val roomId:Int,
    val timeStart:LocalDateTime?,
    val timeEnd:LocalDateTime?,
    val roomName:String,
    val isClosed:Boolean
)