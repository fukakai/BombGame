package com.example.bombgame.repository

import com.example.bombgame.GameConstants
import com.example.bombgame.game.BombConstants
import com.example.bombgame.game.data.BombLiveProperties
import com.example.bombgame.game.dto.Player
import com.example.bombgame.game.dto.Room
import com.example.bombgame.game.holder.FirestoreHolder
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class GameRepository(gameIdParam: String) : FirestoreHolder.AndroidFirestoreInterface {
    private val db = Firebase.firestore
    private val bombLiveProperties: BombLiveProperties = BombLiveProperties.getInstance()
    private var roomDocument: DocumentReference
    private var gameId: String

    init {
        gameId = gameIdParam
        roomDocument = db.collection(GameConstants.ROOMS).document(gameId)
    }

    override fun updateCurrentBombOwner(username: String) {
        var room = roomDocument as Room
        room.currentBombOwner = username
//        roomDocument.set(room, SetOptions.merge())
        roomDocument.update(room)
    }

    override fun updateBombSpeed() {
        var room = roomDocument.get() as Room
        room.deltaXCoef = bombLiveProperties.deltaXCoef
        room.deltaYCoef = bombLiveProperties.deltaYCoef
        roomDocument.update(room)
    }

    fun reinitialiseDeltasToDefault() {
        var room = roomDocument.get() as Room
        room.deltaXCoef = BombConstants.INITIAL_BOMB_SPEED
        room.deltaYCoef = BombConstants.INITIAL_BOMB_SPEED
        roomDocument.update(room)
    }

    override fun updateTimeFromBeginning() {
        var room = roomDocument.get() as Room
        room.timeFromBeginning = bombLiveProperties.timeFromBeginning
        roomDocument.update(room)
    }

    fun reinitialiseTimeFromBeginning() {
        var room = roomDocument.get() as Room
        room.timeFromBeginning = 0F;
        roomDocument.update(room)
    }

    fun setEndOfGame(endOfGame: Float) {
        var room = roomDocument.get() as Room
        room.endOfGame = endOfGame;
        roomDocument.update(room)
    }

    fun addPlayerToList(player: Player) {
//        var room = roomDocument.get() as Room
//        room.playerList.add(player)
//        roomDocument.update(room)
    }

    fun listenToUpdates() {
        listenToCurrentBombOwner()
        listenToPlayerList()
        listenToDelta()
        listenToTimeFromBeginning();
        listenToTotalTimeToEnd();
    }

    private fun listenToTotalTimeToEnd() {
        roomDocument.addSnapshotListener { snapshot, e ->
            if (snapshot != null && snapshot.exists()) {
                val room = snapshot.data as Room
                bombLiveProperties.endOfGame = room.endOfGame.toFloat()
            }
        }
    }

    private fun listenToCurrentBombOwner() {
        roomDocument.addSnapshotListener { snapshot, e ->
            if (snapshot != null && snapshot.exists()) {
                bombLiveProperties
                    .setCurrentBombOwner(snapshot.data?.get(GameConstants.BOMB_OWNER).toString());
            }
        }
    }

    private fun listenToTimeFromBeginning() {
        roomDocument.addSnapshotListener { snapshot, e ->
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
                    bombLiveProperties.timeFromBeginning = timeFromBeginning
                }
            }
        }
    }

    private fun listenToPlayerList() {
//        roomDocument.addSnapshotListener { snapshot, e ->
//            if (snapshot != null && snapshot.exists()) {
//                val room = snapshot.data as Room
//                bombLiveProperties.playerList = room.playerList
//            }
//        }
    }

    private fun listenToDelta() {
        roomDocument.addSnapshotListener { snapshot, e ->
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
                    bombLiveProperties.deltaXCoef = deltaXCoef
                    bombLiveProperties.deltaYCoef = deltaYCoef
                }
            }
        }
    }
}