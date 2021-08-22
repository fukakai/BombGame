package com.example.bombgame.repository

import android.content.ContentValues.TAG
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.bombgame.data.dto.Player
import com.example.bombgame.data.dto.Room
import com.example.bombgame.models.Subscription
import com.example.bombgame.utils.Constants.GAME_STARTED_KEY
import com.example.bombgame.utils.Constants.PLAYER_LIST_COLLECTION
import com.example.bombgame.utils.Constants.ROOMS_COLLECTION
import com.google.firebase.firestore.ListenerRegistration
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.ktx.toObject
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.tasks.await


class RoomRepository private constructor() {

    private val db = Firebase.firestore
    private val roomListLiveData = MutableLiveData<List<Room>>()
    private val currentRoomLiveData = MutableLiveData<Room>()
    private val playerListLiveData = MutableLiveData<List<Player>>()

    private lateinit var roomSubscription: ListenerRegistration
    private lateinit var playerListSubscription: ListenerRegistration
    private lateinit var roomListSubscription: ListenerRegistration

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
     * Add a room to the firestore database and add the player that created the room to
     * the players list.
     * @param room The room to add
     * @param player The player that created the room.
     */
    fun addRoom(room: Room, player: Player) {
        db.collection(ROOMS_COLLECTION)
            .document(room.gameId)
            .set(room)
            .addOnSuccessListener {
                Log.d(TAG, "Room added with ID ${room.gameId}")
                addPlayerCollection(room.gameId, player)
            }
            .addOnFailureListener { e ->
                Log.w(TAG, "Error adding room", e)
            }
    }

    private fun addPlayerCollection(gameId: String, player: Player) {
        db.collection(ROOMS_COLLECTION)
            .document(gameId)
            .collection(PLAYER_LIST_COLLECTION)
            .document(player.username)
            .set(player)

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
     * Update or add a player to a room in the firestore database.
     * @param gameId The room ID.
     * @param player The player to update.
     */
    fun updatePlayer(gameId: String, player: Player) {
        db.collection(ROOMS_COLLECTION)
            .document(gameId)
            .collection(PLAYER_LIST_COLLECTION)
            .document(player.username)
            .set(player)
            .addOnSuccessListener {
                Log.d(TAG, "Player ${player.username} added.")
            }
            .addOnFailureListener { e ->
                Log.w(TAG, "Error adding player ${player.username}.", e)
            }
    }

    /**
     * Update or add a player to a room in the firestore database.
     * @param gameId The room ID.
     * @param player The player to update.
     */
    fun updatePlayerUsername(gameId: String, currentUsername: String, player: Player) {
        deletePlayerFromRoom(currentUsername, gameId)
        updatePlayer(gameId, player)
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
     * Indicate if a username is already taken in a room.
     * @param id the id of the room you want to get
     * @param username The player's username
     * @return true if the username is taken else false.
     */
    suspend fun isUsernameTaken(id: String, username: String): Boolean {
        return try {
            (!db.collection(ROOMS_COLLECTION)
                .document(id)
                .collection(PLAYER_LIST_COLLECTION)
                .whereEqualTo("username", username)
                .get()
                .await()
                .isEmpty)
        } catch (e: Exception) {
            Log.w(TAG, "Error getting player list of room $id.", e)
            false
        }
    }

    /**
     * Listen to the firestore database and update the roomList every time it changes.
     */
    fun listenToRoomList() {
        roomListSubscription = db.collection(ROOMS_COLLECTION)
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
     * @param playerUsername The deleted player's username.
     * @param roomId The room ID to delete the player from.
     */
    fun deletePlayerFromRoom(playerUsername: String, roomId: String) {
        db.collection(ROOMS_COLLECTION)
            .document(roomId)
            .collection(PLAYER_LIST_COLLECTION)
            .document(playerUsername)
            .delete()
            .addOnSuccessListener {
                Log.d(TAG, "Player $playerUsername successfully deleted.")
            }
            .addOnFailureListener { e ->
                Log.w(TAG, "Error deleting player $playerUsername", e)
            }
    }

    /**
     * Update the gameStarted value in firestore.
     * @param gameId The current room ID.
     * @param value Indicate if the game is started or not.
     */
    fun updateStartedGame(gameId: String, value: Boolean) {
        db.collection(ROOMS_COLLECTION)
            .document(gameId)
            .update(GAME_STARTED_KEY, value)
    }

    /**
     * Listen to live updates of a room in firestore.
     * @param gameId The current room ID.
     */
    fun listenToRoom(gameId: String) {
        roomSubscription = db.collection(ROOMS_COLLECTION)
            .document(gameId)
            .addSnapshotListener { snapshot, e ->
                if (e != null) {
                    Log.w(TAG, "Could not listen to room $gameId.", e)
                    return@addSnapshotListener
                }
                currentRoomLiveData.value = snapshot?.toObject<Room>()
            }
    }

    /**
     * Listen to live updates of the players list in a room in firestore.
     * @param gameId The room ID.
     */
    fun listenToPlayerList(gameId: String) {
        playerListSubscription = db.collection(ROOMS_COLLECTION)
            .document(gameId)
            .collection(PLAYER_LIST_COLLECTION)
            .addSnapshotListener { snapshot, e ->
                if (e != null) {
                    Log.w(TAG, "Could not listen to room $gameId.", e)
                    return@addSnapshotListener
                }
                playerListLiveData.value = if (snapshot != null && !snapshot.isEmpty) {
                    snapshot.documents.mapNotNull { it.toObject<Player>() }
                } else {
                    emptyList()
                }
            }
    }

    /**
     * Unsubscribe from firestore.
     * @param subscription The subscription to unsubscribe from.
     */
    fun unsubscribe(subscription: Subscription) {
        when (subscription) {
            Subscription.ROOM_LIST -> roomListSubscription.remove()
            Subscription.PLAYER_LIST -> roomSubscription.remove()
            Subscription.ROOM -> roomSubscription.remove()
        }
    }
}
