package com.example.bombgame.game

import android.os.Bundle
import com.badlogic.gdx.backends.android.AndroidApplication
import com.badlogic.gdx.backends.android.AndroidApplicationConfiguration
import com.example.bombgame.game.holder.FirestoreHolder
import com.example.bombgame.repository.GameRepository

class GameActivity : AndroidApplication() {
//    val playerList = ArrayList<PlayerModel>();

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val config = AndroidApplicationConfiguration()
        FirestoreHolder.setFirestoreDatabase(GameRepository())
//
//        playerList.add(PlayerModel().withId(0).withName("Ludwig"))
//        playerList.add(PlayerModel().withId(1).withName("Florian"))
//        playerList.add(PlayerModel().withId(2).withName("Romain"))

        initialize(BombGame(), config)
    }
}