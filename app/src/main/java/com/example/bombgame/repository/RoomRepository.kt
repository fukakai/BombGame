package com.example.bombgame.repository

import android.content.ContentValues
import android.content.ContentValues.TAG
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.bombgame.data.dto.Player
import com.example.bombgame.data.dto.Room
import com.example.bombgame.utils.Constants.PLAYER_LIST_KEY
import com.example.bombgame.utils.Constants.ROOMS_COLLECTION
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.ktx.toObject
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.tasks.await


class RoomRepository private constructor() {

    private val db = Firebase.firestore
    private val roomListLiveData = MutableLiveData<List<Room>>()
    private val currentRoomLiveData = MutableLiveData<Room>()
    private val playerListLiveData = MutableLiveData<List<Player>>()

    init {
        listenToRoomList()
    }

    fun getRoomListObserver() = roomListLiveData as LiveData<List<Room>>
    fun getCurrentRoomObserver() = currentRoomLiveData as LiveData<Room>
    fun getPlayerListObserver() = playerListLiveData as LiveData<List<Player>>

    companion object {
        @Volatile
        private var instance: RoomRepository? = null

        fun getInstance() =
            instance ?: synchronized(this) {
                instance ?: RoomRepository().also { instance = it }
            }
    }

    /**
     * Add a room to the firestore database.
     * @param room The room to add
     */
    fun addRoom(room: Room) {
        db.collection(ROOMS_COLLECTION)
            .document(room.gameId)
            .set(room)
            .addOnSuccessListener { Log.d(TAG, "Room added with ID ${room.gameId}") }
            .addOnFailureListener { e ->
                Log.w(TAG, "Error adding room", e)
            }
    }

    /**
     * Delete a room from the firestore database.
     * @param id The id of the room to delete.
     */
    fun deleteRoom(id: String) {
        db.collection(ROOMS_COLLECTION)
            .document(id)
            .delete()
            .addOnSuccessListener {
                Log.d(TAG, "Room $id successfully deleted.")
            }
            .addOnFailureListener { e ->
                Log.w(TAG, "Error deleting room with id $id", e)
            }
    }

    /**
     * Add a player to a room with the firestore database.
     * @param room The player to add
     */
    fun updatePlayersList(gameId: String, playersList: List<Player>) {
        db.collection(ROOMS_COLLECTION)
            .document(gameId)
            .update(PLAYER_LIST_KEY, playersList)
            .addOnSuccessListener {
                Log.d(TAG, "Player added")
            }
            .addOnFailureListener { e ->
                Log.w(TAG, "Error adding player", e)
            }
    }

    /**
     * Delete all rooms from the firestore database.
     */
    fun deleteAllRooms() {
        db.collection(ROOMS_COLLECTION)
            .get()
            .addOnSuccessListener { rooms ->
                for (room in rooms) {
                    this.deleteRoom(room.id)
                }
                Log.d(TAG, "Successfully deleted all rooms.")
            }
            .addOnFailureListener { e ->
                Log.w(TAG, "Error deleting all rooms", e)
            }
    }

    /**
     * Return a list containing every rooms in the firestore database.
     * @return A List of all the rooms.
     */
    suspend fun getAllRooms(): List<Room> {
        return try {
            db.collection(ROOMS_COLLECTION)
                .get()
                .await()
                .documents
                .mapNotNull { it.toObject<Room>() }
        } catch (e: Exception) {
            Log.w(TAG, "Error getting room list.", e)
            emptyList()
        }
    }

    /**
     * Return a room from the firestore database.
     * @param id the id of the room you want to get
     * @return A room.
     */
    suspend fun getRoom(id: String): Room? {
        return try {
            (db.collection(ROOMS_COLLECTION)
                .document(id)
                .get()
                .await()
                .toObject())
        } catch (e: Exception) {
            Log.w(TAG, "Error getting room.", e)
            null
        }
    }

    /**
     * Listen to the firestore database and update the roomList every time it changes.
     */
    private fun listenToRoomList() {
        db.collection(ROOMS_COLLECTION)
            .addSnapshotListener { snapshot, e ->
                if (e != null) {
                    Log.w(TAG, "Could not listen to rooms collection.", e)
                    return@addSnapshotListener
                }

                roomListLiveData.value = if (snapshot != null && !snapshot.isEmpty) {
                    snapshot.documents.mapNotNull { it.toObject<Room>() }
                } else {
                    emptyList()
                }
            }
    }

    /**
     * Delete a player of a room from the firestore database.
     * @param id The id of the room to delete.
     */
    fun deletePlayerFromRoom(playerUsername: String, roomId: String) {
        val updatedList = playerListLiveData.value?.filter { it.username != playerUsername }

        db.collection(ROOMS_COLLECTION)
            .document(roomId)
            .update(PLAYER_LIST_KEY, updatedList)
            .addOnSuccessListener {
                Log.d(ContentValues.TAG, "Player $playerUsername successfully deleted.")
            }
            .addOnFailureListener { e ->
                Log.w(ContentValues.TAG, "Error deleting player $playerUsername", e)
            }
    }

    /**
     * Listen to the firestore database for players and update the playerList every time it changes.
     */
    fun listenToPlayerList(gameId: String) {
        db.collection(ROOMS_COLLECTION)
            .document(gameId)
            .addSnapshotListener { snapshot, e ->
                if (e != null) {
                    Log.w(ContentValues.TAG, "Could not listen to players collection.", e)
                    return@addSnapshotListener
                }

                val playersList = snapshot?.toObject<Room>()?.playerList
                playerListLiveData.value = if (!playersList.isNullOrEmpty()) {
                    playersList
                } else {
                    emptyList()
                }
            }
    }
}
