package com.example.bombgame.repository

import com.example.bombgame.GameConstants
import com.example.bombgame.game.data.BombLiveProperties
import com.example.bombgame.game.holder.FirestoreHolder
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class GameRepository : FirestoreHolder.AndroidFirestoreInterface {
    private val db = Firebase.firestore

    override fun updateCurrentBombOwner(string: String?) {
        db.collection(GameConstants.ROOMS)
            .document(BombLiveProperties.getInstance().gameId)
            .update(GameConstants.BOMB_OWNER, string)
    }

    override fun updateBombSpeed() {
        db.collection(GameConstants.ROOMS)
            .document(BombLiveProperties.getInstance().gameId)
            .update(GameConstants.DELTA_X_COEF, BombLiveProperties.getInstance().deltaXCoef)
        db.collection(GameConstants.ROOMS)
            .document(BombLiveProperties.getInstance().gameId)
            .update(GameConstants.DELTA_Y_COEF, BombLiveProperties.getInstance().deltaYCoef)
    }

    override fun updateTimeFromBeginning() {
        db.collection(GameConstants.ROOMS)
            .document(BombLiveProperties.getInstance().gameId)
            .update(GameConstants.TIME_FROM_BEGINNING, BombLiveProperties.getInstance().timeFromBeginning)
    }

    fun updatePlayerList(playerList: ArrayList<String>?) {
        db.collection(GameConstants.ROOMS)
            .document(BombLiveProperties.getInstance().gameId)
            .update(GameConstants.PLAYER_LIST, playerList)
    }

    fun listenToUpdates() {
        listenToCurrentBombOwner()
        listenToPlayerList()
        listenToDelta()
        listenToTimeFromBeginning();
    }

    private fun listenToCurrentBombOwner() {
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

    private fun listenToTimeFromBeginning() {
        val docRef =
            db.collection(GameConstants.ROOMS)
                .document(BombLiveProperties.getInstance().gameId)
        docRef.addSnapshotListener { snapshot, e ->
            if (snapshot != null && snapshot.exists()) {
                BombLiveProperties.getInstance()
                    .timeFromBeginning =
                    snapshot.data?.get(GameConstants.TIME_FROM_BEGINNING) as Float;
            }
        }
    }

    private fun listenToPlayerList() {
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

    private fun listenToDelta() {
        val docRef =
            db.collection(GameConstants.ROOMS)
                .document(BombLiveProperties.getInstance().gameId)
        docRef.addSnapshotListener { snapshot, e ->
            if (snapshot != null && snapshot.exists()) {
                BombLiveProperties.getInstance().deltaXCoef =
                    snapshot.data?.get(GameConstants.DELTA_X_COEF) as Float
                BombLiveProperties.getInstance().deltaYCoef =
                    snapshot.data?.get(GameConstants.DELTA_Y_COEF) as Float
            }
        }
    }
}