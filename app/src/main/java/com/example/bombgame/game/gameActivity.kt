package com.example.bombgame.game

import android.os.Bundle
import com.badlogic.gdx.backends.android.AndroidApplication
import com.badlogic.gdx.backends.android.AndroidApplicationConfiguration
import com.example.bombgame.game.data.BombLiveProperties
import com.example.bombgame.game.dto.Player
import com.example.bombgame.game.holder.FirestoreHolder
import com.example.bombgame.repository.GameRepository
import java.util.*

class GameActivity : AndroidApplication() {
    private val gameRepository: GameRepository = GameRepository("YGQBCC")
    private val bombLiveProperties: BombLiveProperties = BombLiveProperties.getInstance()
    private val random = Random()
    private val gameId = "YGQBCC";

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val config = AndroidApplicationConfiguration()
        // pour utiliser le repository dans le jeu
        FirestoreHolder.setFirestoreDatabase(gameRepository)

        // Ajout d'un nouveau joueur au pseudo aléatoire pour le moment
        val player = Player(getRandomString())

        bombLiveProperties.gameId = gameId
        bombLiveProperties.setLocalPlayer(player)

        gameRepository.addPlayerToList(player)

        // le joueur qui a la bombe est défini aléatoirement àprès la connection de chaque joueur jusqu'au dernier
        gameRepository.updateCurrentBombOwner(
            ""
        )
        gameRepository.reinitialiseTimeFromBeginning()
        gameRepository.reinitialiseDeltasToDefault()
        gameRepository.setEndOfGame(randomEnd())

        // Game Live Properties Listener
        gameRepository.listenToUpdates()

        // Game Initialization
        initialize(BombGame(), config)
    }

    /**
     * Fonctions temporaires pour générer un pseudo aléatoire en attendant d'avoir celui configuré par le joueur
     */
    fun getRandomString(): String {
        val length = 10;
        val charset = ('a'..'z') + ('A'..'Z') + ('0'..'9')
        return (1..length)
            .map { charset.random() }
            .joinToString("")
    }

    /**
     * Random time before game over
     */
    private fun randomEnd(): Float {
        return BombConstants.MIN_SECONDS_BEFORE_END + random.nextFloat() * (BombConstants.MAX_SECONDS_BEFORE_END - BombConstants.MIN_SECONDS_BEFORE_END)
    }
}