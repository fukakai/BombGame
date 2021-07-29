package com.example.bombgame.data.dto

data class Room(
    var gameId: String= "",
    var bombState: String = "",
    var currentBombOwner: String = "",
    var deltaXCoef: Int = 7,
    var deltaYCoef: Int = 7,
    var endOfGame: Int = 0,
    var timeFromBeginning: Int = 0,
    var playerList: List<Player> = mutableListOf()
)

