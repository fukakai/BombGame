package com.example.bombgame.repository

import android.content.ContentValues.TAG
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.bombgame.data.dto.Room
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.ktx.toObject
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.tasks.await

class RoomRepository private constructor() {

    private val roomCollection = "rooms"
    private val db = Firebase.firestore
    private val roomListLiveData = MutableLiveData<List<Room>>()

    init {
        listenToRoomList()
    }

    fun getRoomListObserver() = roomListLiveData as LiveData<List<Room>>

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
        db.collection(roomCollection)
            .add(room)
            .addOnSuccessListener { roomReference ->
                Log.d(TAG, "Room added with ID ${roomReference.id}")
            }
            .addOnFailureListener { e ->
                Log.w(TAG, "Error adding room", e)
            }
    }

    /**
     * Delete a room from the firestore database.
     * @param id The id of the room to delete.
     */
    fun deleteRoom(id: String) {
        db.collection(roomCollection)
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
     * Delete all rooms from the firestore database.
     */
    fun deleteAllRooms() {
        db.collection(roomCollection)
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
            db.collection(roomCollection)
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
     * Listen to the firestore database and update the roomList every time it changes.
     */
    private fun listenToRoomList() {
        db.collection(roomCollection)
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

}
