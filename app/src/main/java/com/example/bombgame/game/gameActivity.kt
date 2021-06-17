package com.example.bombgame.game

import android.os.Bundle
import com.badlogic.gdx.backends.android.AndroidApplication
import com.badlogic.gdx.backends.android.AndroidApplicationConfiguration
import com.example.bombgame.game.data.BombLiveProperties
import com.example.bombgame.game.holder.FirestoreHolder
import com.example.bombgame.repository.GameRepository

class GameActivity : AndroidApplication() {
    private val gameRepository: GameRepository = GameRepository()
    val playerList = ArrayList<String>();

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val config = AndroidApplicationConfiguration()
        FirestoreHolder.setFirestoreDatabase(GameRepository())

        // Game Configuration
        playerList.add("Romain")
        playerList.add("Ludwig")
        playerList.add("Florian")

        BombLiveProperties.getInstance().setLocalPlayer("Romain")
        BombLiveProperties.getInstance().setGameId("QTUUOH")

        gameRepository.updatePlayerList(playerList)
        gameRepository.updateCurrentBombOwner("Romain")
        gameRepository.reinitialiseTimeFromBeginning()
        gameRepository.reinitialiseDeltasToDefault()

        // Game Live Properties Listener
        gameRepository.listenToUpdates()

        // Game Initialization
        initialize(BombGame(), config)
    }

}