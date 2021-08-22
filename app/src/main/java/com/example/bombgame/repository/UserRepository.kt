package com.example.bombgame.repository

import android.content.ContentValues.TAG
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.bombgame.data.dto.User
import com.example.bombgame.utils.Constants
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.ktx.toObject
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.tasks.await

class UserRepository private constructor() {

    private val db = Firebase.firestore
    private val userLiveData = MutableLiveData<User>()

    companion object {
        @Volatile
        private var instance: UserRepository? = null

        fun getInstance() =
            instance ?: synchronized(this) {
                instance ?: UserRepository().also { instance = it }
            }
    }

    fun getUserObserver() = userLiveData as LiveData<User>

    /**
     * Add a user to the users collection.
     * @param user: The user to add.
     */
    fun addUser(user: User) {
        db.collection(Constants.USERS_COLLECTION)
            .document(user.id)
            .set(user)
    }

    /**
     * Get a user from firestore using its ID.
     * @param id: The user's ID.
     */
    suspend fun getUser(id: String): User? {
        return db.collection(Constants.USERS_COLLECTION)
            .document(id)
            .get()
            .await()
            .toObject()
    }

    /**
     * Update a user in firestore.
     * @param user: The user to update.
     */
    fun updateUser(user: User) {
        db.collection(Constants.USERS_COLLECTION)
            .document(user.id)
            .set(user)
    }

    /**
     * Delete a user from firestore using its ID.
     * @param id: The user's ID.
     */
    fun deleteUser(id: String) {
        db.collection(Constants.USERS_COLLECTION)
            .document(id)
            .delete()
            .addOnSuccessListener {
                Log.d(TAG, "Successfully deleted user $id")
            }
            .addOnFailureListener { e ->
                Log.w(TAG, "Error while deleting user $id")
            }
    }

    /**
     * Listen to a user from firestore using its ID.
     * @param id: The user's ID.
     */
    fun listenToUser(id: String) {
        db.collection(Constants.USERS_COLLECTION)
            .document(id)
            .addSnapshotListener { snapshot, e ->
                if (e != null) {
                    Log.w(TAG, "Could not listen user $id.", e)
                    return@addSnapshotListener
                }
                userLiveData.value = snapshot!!.toObject<User>()
            }
    }
}