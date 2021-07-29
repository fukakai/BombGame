package com.example.bombgame.data.dto

data class Player (var username: String = "", var ready: Boolean = false) {
    override fun toString(): String {
        return "User $username, is ready : $ready"
    }
}