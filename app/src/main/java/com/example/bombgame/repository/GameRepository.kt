package com.example.bombgame.repository

import com.example.bombgame.GameConstants
import com.example.bombgame.game.data.BombLiveProperties
import com.example.bombgame.game.holder.FirestoreHolder
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class GameRepository : FirestoreHolder.AndroidFirestoreInterface {
    private val db = Firebase.firestore

    override fun updateBombDatas(string: String?) {
        db.collection(GameConstants.ROOMS)
            .document(BombLiveProperties.getInstance().gameId)
            .update("bombLiveDataTest", string)
    }

    fun listenToUpdates() {
        listenToCurrentPlayer()
    }

    private fun listenToCurrentPlayer() {
        val docRef =
            db.collection(GameConstants.ROOMS)
                .document(BombLiveProperties.getInstance().gameId)
        docRef.addSnapshotListener { snapshot, e ->
            if (snapshot != null && snapshot.exists()) {
                BombLiveProperties.getInstance()
                    .setCurrentBombOwner(snapshot.data?.get(GameConstants.BOMB_OWNER).toString());
            }
        }
    }

    fun listenToPlayerList() {
        val docRef =
            db.collection(GameConstants.ROOMS)
                .document(BombLiveProperties.getInstance().gameId)
        docRef.addSnapshotListener { snapshot, e ->
            if (snapshot != null && snapshot.exists()) {
                BombLiveProperties.getInstance()
                    .setPlayerList(snapshot.data?.get(GameConstants.PLAYER_LIST) as ArrayList<String>)
            }
        }
    }

    fun updatePlayerList(playerList: ArrayList<String>?) {
        db.collection(GameConstants.ROOMS)
            .document(BombLiveProperties.getInstance().gameId)
            .update(GameConstants.PLAYER_LIST,playerList)
    }
}