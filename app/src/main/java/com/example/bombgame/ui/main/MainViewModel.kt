package com.example.bombgame.ui.main

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.bombgame.data.dto.Room
import com.example.bombgame.repository.RoomRepository
import com.example.bombgame.utils.RoomUtils
import kotlinx.coroutines.launch


class MainViewModel(private val roomRepository: RoomRepository) : ViewModel() {

    private var roomList = listOf<Room>()
    private val roomListLiveData = MutableLiveData<List<Room>>()

    init {
        updateRoomList()
    }

    fun getRoomListObserver() = roomListLiveData as LiveData<List<Room>>

    /**
     * Call the roomRepository to add a room.
     * @param room The room to add.
     */
    fun addRoom(room: Room) {
        generateUniqueRoomId(room)
        roomRepository.addRoom(room)
        updateRoomList()
    }

    /**
     * Call the roomRepository to delete a room using its ID.
     * @param id The room's reference ID from the database.
     */
    fun deleteRoom(id: String) {
        roomRepository.deleteRoom(id)
        updateRoomList()
    }

    /**
     * Call the roomRepository to delete all the rooms.
     */
    fun deleteAllRooms() {
        roomRepository.deleteAllRooms()
        updateRoomList()
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
     * Get the room list from the roomRepository and update the LiveData.
     */
    private fun updateRoomList() {
        viewModelScope.launch {
            roomList = roomRepository.getAllRooms()
            roomListLiveData.value = roomList
        }
    }
}
