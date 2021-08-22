package com.example.bombgame.repository

import com.example.bombgame.GameConstants
import com.example.bombgame.data.dto.Player
import com.example.bombgame.game.BombConstants
import com.example.bombgame.game.data.BombLiveProperties
import com.example.bombgame.game.holder.FirestoreHolder
import com.example.bombgame.utils.Constants.PLAYER_LIST_COLLECTION
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.ktx.toObject
import com.google.firebase.ktx.Firebase
import java.util.*

class GameRepository : FirestoreHolder.AndroidFirestoreInterface {
    private val db = Firebase.firestore
    private val properties = BombLiveProperties.getInstance()

    /**
     * Update the current bomb owner to firestore.
     * @param username : The current bomb owner's username.
     */
    override fun updateCurrentBombOwner(username: String?) {
        db.collection(GameConstants.ROOMS)
            .document(BombLiveProperties.getInstance().gameId)
            .update(GameConstants.BOMB_OWNER, username)
    }

    /**
     * Update the current bomb speed to firestore.
     */
    override fun updateBombSpeed() {
        db.collection(GameConstants.ROOMS)
            .document(BombLiveProperties.getInstance().gameId)
            .update(GameConstants.DELTA_X_COEF, BombLiveProperties.getInstance().deltaXCoef)
        db.collection(GameConstants.ROOMS)
            .document(BombLiveProperties.getInstance().gameId)
            .update(GameConstants.DELTA_Y_COEF, BombLiveProperties.getInstance().deltaYCoef)
    }

    /**
     * Reinitialize bomb's deltas to default values to firestore.
     */
    fun reinitialiseDeltasToDefault() {
        db.collection(GameConstants.ROOMS)
            .document(BombLiveProperties.getInstance().gameId)
            .update(GameConstants.DELTA_X_COEF, BombConstants.INITIAL_BOMB_SPEED)
        db.collection(GameConstants.ROOMS)
            .document(BombLiveProperties.getInstance().gameId)
            .update(GameConstants.DELTA_Y_COEF, BombConstants.INITIAL_BOMB_SPEED)
    }

    /**
     * Update timer from beginning of the game to firestore.
     */
    override fun updateTimeFromBeginning() {
        db.collection(GameConstants.ROOMS)
            .document(BombLiveProperties.getInstance().gameId)
            .update(
                GameConstants.TIME_FROM_BEGINNING,
                BombLiveProperties.getInstance().timeFromBeginning
            )
    }

    /**
     * Reset timer from beginning of the game to firestore.
     */
    fun reinitialiseTimeFromBeginning() {
        db.collection(GameConstants.ROOMS)
            .document(BombLiveProperties.getInstance().gameId)
            .update(GameConstants.TIME_FROM_BEGINNING, 0F)
    }

    /**
     * Set the end of game time to firestore.
     */
    override fun setEndOfGame(end: Int) {
        db.collection(GameConstants.ROOMS)
            .document(BombLiveProperties.getInstance().gameId)
            .update(GameConstants.END_OF_GAME, end)
    }

    fun updatePlayerList(playerList: List<Player>) {
        for (player in playerList) {
            db.collection(GameConstants.ROOMS)
                .document(BombLiveProperties.getInstance().gameId)
                .collection(PLAYER_LIST_COLLECTION)
                .document(player.username)
                .set(player)
        }
    }

    /**
     * Listen to live updates from firestore.
     */
    fun listenToUpdates() {
        listenToCurrentBombOwner()
        listenToPlayerList()
        listenToDelta()
        listenToTimeFromBeginning();
    }

    /**
     * Listen to the current bomb owner from firestore.
     */
    private fun listenToCurrentBombOwner() {
        db.collection(GameConstants.ROOMS)
            .document(BombLiveProperties.getInstance().gameId)
            .addSnapshotListener { snapshot, e ->
                if (snapshot != null && snapshot.exists()) {
                    BombLiveProperties.getInstance().currentBombOwner =
                        snapshot.data?.get(GameConstants.BOMB_OWNER).toString();
                }
            }
    }

    /**
     * Listen to the time from beginning of the game from firestore.
     */
    private fun listenToTimeFromBeginning() {
        db.collection(GameConstants.ROOMS)
            .document(BombLiveProperties.getInstance().gameId)
            .addSnapshotListener { snapshot, e ->
                if (snapshot != null && snapshot.exists()) {
                    var timeFromBeginning: Float? = null;

                    if (snapshot.data?.get(GameConstants.TIME_FROM_BEGINNING) is Long) {
                        timeFromBeginning =
                            (snapshot.data?.get(GameConstants.TIME_FROM_BEGINNING) as Long).toFloat()
                    }

                    if (snapshot.data?.get(GameConstants.TIME_FROM_BEGINNING) is Double) {
                        timeFromBeginning =
                            (snapshot.data?.get(GameConstants.TIME_FROM_BEGINNING) as Double).toFloat()
                    }

                    if (timeFromBeginning != null) {
                        BombLiveProperties.getInstance().timeFromBeginning = timeFromBeginning
                    }
                }
            }
    }

    /**
     * Listen to the game's players list from firestore.
     */
    private fun listenToPlayerList() {
        db.collection(GameConstants.ROOMS)
            .document(BombLiveProperties.getInstance().gameId)
            .collection(PLAYER_LIST_COLLECTION)
            .addSnapshotListener { snapshot, e ->
                if (e != null) {
                    return@addSnapshotListener
                }
                BombLiveProperties.getInstance().playerList = if (snapshot != null && !snapshot.isEmpty) {
                    snapshot.documents.mapNotNull { it.toObject<Player>()?.username } as ArrayList<String>?
                } else null
            }
    }

    /**
     * Listen to the bomb's deltas from firestore.
     */
    private fun listenToDelta() {
        db.collection(GameConstants.ROOMS)
            .document(BombLiveProperties.getInstance().gameId)
            .addSnapshotListener { snapshot, e ->
                if (snapshot != null && snapshot.exists()) {
                    var deltaXCoef: Float? = null;
                    var deltaYCoef: Float? = null;

                    if (snapshot.data?.get(GameConstants.DELTA_X_COEF) is Long) {
                        deltaXCoef =
                            (snapshot.data?.get(GameConstants.DELTA_X_COEF) as Long).toFloat()
                        deltaYCoef =
                            (snapshot.data?.get(GameConstants.DELTA_Y_COEF) as Long).toFloat()
                    }

                    if (snapshot.data?.get(GameConstants.DELTA_X_COEF) is Double) {
                        deltaXCoef =
                            (snapshot.data?.get(GameConstants.DELTA_X_COEF) as Double).toFloat()
                        deltaYCoef =
                            (snapshot.data?.get(GameConstants.DELTA_Y_COEF) as Double).toFloat()
                    }

                    if (deltaXCoef != null && deltaYCoef != null) {
                        BombLiveProperties.getInstance().deltaXCoef = deltaXCoef
                        BombLiveProperties.getInstance().deltaYCoef = deltaYCoef
                    }
                }
            }
    }
}