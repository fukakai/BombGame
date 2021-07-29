package com.example.bombgame.utils

import com.example.bombgame.data.dto.Player

object PlayersUtils {

    fun areAllPlayersReady(listePlayer: List<Player>): Boolean {
        var reponse = true

        listePlayer.forEach { player ->
            if (!player.ready) {
                reponse = false
            }
        }
        return reponse
    }
}