package com.example.bombgame.data.dto

data class Room(
    var bombState: String = "",
    var currentBombOwner: String = "Romain",
    var deltaXCoef: Number = 7,
    var deltaYCoef: Number = 7,
    var endOfGame: Number = 0,
    var timeFromBeginning: Number = 0,
    var playerList: List<Player> = mutableListOf()
)
