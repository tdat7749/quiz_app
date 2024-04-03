package com.example.client.model

import java.time.LocalDate
import java.util.Date

data class Room(
    val id:Int,
    val host:User,
    val timeStart:String,
    val timeEnd:String,
    val quiz:Quiz,
    val roomPin:String,
    val createdAt:String
)


data class CreateRoom(
    val quizId:Int,
    val timeStart: LocalDate?,
    val timeEnd:LocalDate?,
    val roomName:String
)

data class EditRoom(
    val roomId:Int,
    val timeStart:LocalDate?,
    val timeEnd:LocalDate?,
    val roomName:String
)