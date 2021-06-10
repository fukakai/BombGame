package com.example.bombgame.repository

import com.example.bombgame.game.holder.FirestoreHolder
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class GameRepository : FirestoreHolder.AndroidFirestoreInterface {
    private val db = Firebase.firestore

    override fun updateBombDatas(string: String?) {
        db.collection("rooms")
            .document("QTUUOH")
            .update("bombLiveDataTest", string)
    }
}