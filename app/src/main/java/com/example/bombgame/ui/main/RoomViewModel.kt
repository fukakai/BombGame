package com.example.bombgame.ui.main

import androidx.lifecycle.ViewModel
import com.example.bombgame.data.dto.Player
import com.example.bombgame.data.dto.Room
import com.example.bombgame.data.dto.User
import com.example.bombgame.models.Subscription
import com.example.bombgame.repository.RoomRepository
import com.example.bombgame.utils.RoomUtils


class RoomViewModel(private val roomRepository: RoomRepository) : ViewModel() {

    private var roomList = listOf<Room>()
    private val roomListLiveData = roomRepository.getRoomListObserver()
    private val currentRoomLiveData = roomRepository.getCurrentRoomObserver()
    private val playerListLiveData = roomRepository.getPlayerListObserver()

    fun getRoomListObserver() = roomListLiveData
    fun getCurrentRoomObserver() = currentRoomLiveData
    fun getPlayerListObserver() = playerListLiveData

    /**
     * Call the roomRepository to add a room.
     * @param room The room to add.
     */
    fun addRoom(user: User): List<String> {
        val room = Room()
        val player = Player(user.username)
        generateUniqueRoomId(room)
        roomRepository.addRoom(room, player)
        return arrayListOf(player.username, room.gameId)
    }

    /**
     * Call the roomRepository to get a room.
     * @param id The room to get.
     */
    suspend fun isUsernameTaken(id: String, username: String): Boolean {
        return roomRepository.isUsernameTaken(id, username)
    }

    /**
     * Call the roomRepository to add a player to a room.
     * @param player The player to add.
     * aprama room the room where to add the player.
     */
    fun addPlayerToRoom(user: User, gameId: String) {
        roomRepository.updatePlayer(gameId, Player(user.username))
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
     * Call the roomRepository to delete a player using its ID.
     * @param id The player's reference ID from the database.
     */
    fun deletePlayer(playerUsername: String, roomId: String) {
        roomRepository.deletePlayerFromRoom(playerUsername, roomId)

        val currentPlayerList = playerListLiveData.value
        if (currentPlayerList?.size == 1) {
            roomRepository.deleteRoom(roomId)
        }
    }

    fun switchReady(gameId: String, playerUsername: String) {
        val playerList = playerListLiveData.value
        if (playerList != null) {
            val player = playerList.find { it.username == playerUsername }!!
            player.ready = !player.ready
            roomRepository.updatePlayer(gameId, player)
        }
    }

    fun updateStartedGame(gameId: String, value: Boolean) {
        roomRepository.updateStartedGame(gameId, value)
    }

    fun listenToRoom(gameId: String) {
        roomRepository.listenToRoom(gameId)
    }

    fun listenToPlayerList(gameId: String) {
        roomRepository.listenToPlayerList(gameId)
    }

    fun unsubscribe(subscriptions: List<Subscription>) {
        for (subscription in subscriptions) {
            roomRepository.unsubscribe(subscription)
        }
    }

    fun unsubscribe(subscription: Subscription) {
        roomRepository.unsubscribe(subscription)
    }

    fun listenToRoomList() {
        roomRepository.listenToRoomList()
    }
}

