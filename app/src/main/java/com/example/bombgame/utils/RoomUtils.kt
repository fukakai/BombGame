package com.example.bombgame.utils

import com.example.bombgame.data.dto.Room

object RoomUtils {

    private val alphabet = listOf(
        "A",
        "B",
        "C",
        "D",
        "E",
        "F",
        "G",
        "H",
        "I",
        "J",
        "K",
        "L",
        "M",
        "N",
        "O",
        "P",
        "Q",
        "R",
        "S",
        "T",
        "U",
        "V",
        "X",
        "Y",
        "Z"
    )

    /**
     * Generate a random id for a room.
     * @return A random 6-letters string.
     */
    fun generateId(): String {
        var id = ""
        for (x in 0 until 6) {
            id += alphabet.random()
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
