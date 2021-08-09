package com.example.bombgame

import android.os.Bundle
import com.badlogic.gdx.backends.android.AndroidApplication
import com.badlogic.gdx.backends.android.AndroidApplicationConfiguration
import com.example.bombgame.data.dto.Player
import com.example.bombgame.game.BombConstants
import com.example.bombgame.game.BombGame
import com.example.bombgame.game.data.BombLiveProperties
import com.example.bombgame.game.holder.FirestoreHolder
import com.example.bombgame.repository.GameRepository
import com.example.bombgame.utils.Constants
import kotlin.random.Random.Default.nextInt

class GameActivity : AndroidApplication() {
    private val gameRepository: GameRepository = GameRepository()

    private var playerList: List<Player> = mutableListOf()
    private lateinit var playerUsername: String
    private lateinit var gameId: String
    private val properties = BombLiveProperties.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val config = AndroidApplicationConfiguration()
        FirestoreHolder.setFirestoreDatabase(GameRepository())

        // Game Configuration
        val intentGameActivity = intent
        val bundle = intentGameActivity.extras
        if (bundle?.getString(Constants.ROOM_ID_KEY) != null) {
            gameId = bundle.getString(Constants.ROOM_ID_KEY)!!
        }

        if (bundle?.getString(Constants.PLAYER_USERNAME_KEY) != null) {
            playerUsername = bundle.getString(Constants.PLAYER_USERNAME_KEY)!!
        }


        // Ajout d'un nouveau joueur au pseudo aléatoire pour le moment

        properties.setGameId(gameId)
        properties.setLocalPlayer(playerUsername)

        // le joueur qui a la bombe est défini aléatoirement àprès la connection de chaque joueur jusqu'au dernier
        gameRepository.reinitialiseTimeFromBeginning()
        gameRepository.reinitialiseDeltasToDefault()
        gameRepository.setEndOfGame(randomEnd())

        // Game Live Properties Listener
        gameRepository.listenToUpdates()

        // Game Initialization
        initialize(BombGame(), config)
    }

    /** Random time before game over  */
    private fun randomEnd(): Int {
        return nextInt(BombConstants.MIN_SECONDS_BEFORE_END.toInt(),
            BombConstants.MAX_SECONDS_BEFORE_END.toInt())
    }
}