package com.example.bombgame.utils

import com.example.bombgame.data.dto.Room

object RoomUtils {

    private val vowels = listOf(
        "A",
        "E",
        "I",
        "O",
        "U",
        "Y"
    )

    private val consonants = listOf(
        "B",
        "C",
        "D",
        "F",
        "G",
        "H",
        "J",
        "K",
        "L",
        "M",
        "N",
        "P",
        "Q",
        "R",
        "S",
        "T",
        "V",
        "X",
        "Z"
    )

    private val alphabet = listOf(vowels, consonants)

    /**
     * Generate a random id for a room.
     * @return A random 6-letters string.
     */
    fun generateId(): String {
        var id = ""
        val startingType = alphabet.random()
        val secondType = if (startingType == vowels) consonants else vowels
        for (x in 0 until 6) {
            id += if (x % 2 == 0) {
                startingType.random()
            } else {
                secondType.random()
            }
        }
        return id
    }

    /**
     * Indicate if the gameId is in the current room list.
     * @param gameId The game ID of a room.
     * @param roomList The list of rooms to check if the gameId exists.
     * @return Boolean
     */
    fun isExistingRoom(gameId: String, roomList: List<Room>): Boolean {
        return (true in roomList.map { it.gameId == gameId })
    }
}
