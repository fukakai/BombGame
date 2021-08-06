package com.example.bombgame.ui.main

import androidx.lifecycle.ViewModel
import com.example.bombgame.data.dto.Player
import com.example.bombgame.data.dto.Room
import com.example.bombgame.data.dto.User
import com.example.bombgame.repository.RoomRepository
import com.example.bombgame.utils.RoomUtils


class RoomViewModel(private val roomRepository: RoomRepository) : ViewModel() {

    private var roomList = listOf<Room>()
    private var playerList = listOf<Player>()
    private val roomListLiveData = roomRepository.getRoomListObserver()
    private val currentRoomLiveData = roomRepository.getCurrentRoomObserver()
    private val playerListLiveData = roomRepository.getPlayerListObserver()
    private val gameStartedLiveData = roomRepository.getGameStartedObserver()

    fun getRoomListObserver() = roomListLiveData
    fun getCurrentRoomObserver() = currentRoomLiveData
    fun getGameStartedObserver() = gameStartedLiveData

    /**
     * Call the roomRepository to add a room.
     * @param room The room to add.
     */
    fun addRoom(user: User): List<String> {
        val room = Room()
        val player = Player(user.username)
        generateUniqueRoomId(room)
        room.playerList += player
        roomRepository.addRoom(room)
        return arrayListOf(player.username, room.gameId)
    }

    /**
     * Call the roomRepository to get a room.
     * @param id The room to get.
     */
    suspend fun getRoom(id: String): Room? {
        return roomRepository.getRoom(id)
    }

    /**
     * Call the roomRepository to add a player to a room.
     * @param player The player to add.
     * aprama room the room where to add the player.
     */
    fun addPlayerToRoom(user: User, room: Room): Boolean {
        val player = Player(user.username)
        return if (isUsernameTaken(player.username, room)) {
            false
        } else {
            room.playerList += player
            roomRepository.updatePlayersList(room.gameId, room.playerList)
            true
        }
    }

    /**
     * Call the roomRepository to delete a room using its ID.
     * @param id The room's reference ID from the database.
     */
    fun deleteRoom(id: String) {
        roomRepository.deleteRoom(id)
    }

    /**
     * Call the roomRepository to delete all the rooms.
     */
    fun deleteAllRooms() {
        roomRepository.deleteAllRooms()
    }

    /**
     * Generate a unique gameId for a room.
     * @param room The room that needs a gameId.
     */
    private fun generateUniqueRoomId(room: Room) {
        var gameId: String = RoomUtils.generateId()
        while (RoomUtils.isExistingRoom(gameId, roomList)) {
            gameId = RoomUtils.generateId()
        }
        room.gameId = gameId
    }

    /**
     * Indicate if the username is already taken in the room.
     * @param username The username
     * @param room The room to check
     * @return True if the username is taken in this room, false if not.
     * */
    private fun isUsernameTaken(username: String, room: Room): Boolean {
        return (true in room.playerList.map { it.username == username })
    }

    fun getPlayerListObserver() = playerListLiveData

    /**
     * Call the roomRepository to delete a player using its ID.
     * @param id The player's reference ID from the database.
     */
    fun deletePlayer(playerUsername: String, roomId: String) {
        roomRepository.deletePlayerFromRoom(playerUsername, roomId)
    }

    fun listenToPlayersList(gameId: String) {
        roomRepository.listenToPlayerList(gameId)
    }

    fun switchReady(gameId: String, playerUsername: String) {
        val playersList = playerListLiveData.value
        if (playersList != null) {
            val isReady = playersList.find { it.username == playerUsername }?.ready
            playersList.find { it.username == playerUsername }?.ready = !isReady!!
            roomRepository.updatePlayersList(gameId, playersList)
        }
    }

    fun updateStartedGame(gameId: String, value: Boolean) {
        roomRepository.updateStartedGame(gameId, value)
    }

    fun listenToGameStarted(gameid: String) {
        roomRepository.listenToGameStarted(gameid)
    }
}
