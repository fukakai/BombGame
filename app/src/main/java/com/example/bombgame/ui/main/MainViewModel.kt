package com.example.bombgame.ui.main

import androidx.lifecycle.ViewModel
import com.example.bombgame.data.dto.Room
import com.example.bombgame.repository.RoomRepository
import com.example.bombgame.utils.RoomUtils


class MainViewModel(private val roomRepository: RoomRepository) : ViewModel() {

    private var roomList = listOf<Room>()
    private val roomListLiveData = roomRepository.getRoomListObserver()

    fun getRoomListObserver() = roomListLiveData

    /**
     * Call the roomRepository to add a room.
     * @param room The room to add.
     */
    fun addRoom(room: Room) {
        generateUniqueRoomId(room)
        roomRepository.addRoom(room)
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
}
