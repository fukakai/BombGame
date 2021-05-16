package com.example.bombgame.data.dto

data class Room(var gameId: String = "") {
    override fun toString(): String {
        return "Room $gameId"
    }
}

