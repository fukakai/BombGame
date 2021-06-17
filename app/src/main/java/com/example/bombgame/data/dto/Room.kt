package com.example.bombgame.data.dto

data class Room(var gameId: String = "", var currentBombOwner: String = "Romain") {

    override fun toString(): String {
        return "Room $gameId"
    }
}

