package com.example.bombgame.data.dto

data class User(var username: String = "", var id: String = "") {
    override fun toString(): String {
        return "User $username, id : $id"
    }
}