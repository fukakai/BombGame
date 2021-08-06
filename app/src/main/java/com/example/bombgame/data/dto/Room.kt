package com.example.bombgame.data.dto

data class Room(
    var gameId: String = "",
    var bombState: String = "",
    var currentBombOwner: String = "",
    var deltaXCoef: Double = 7.0,
    var deltaYCoef: Double = 7.0,
    var endOfGame: Double = 0.0,
    var timeFromBeginning: Double = 0.0,
    var playerList: List<Player> = mutableListOf()
)

