package com.example.bombgame.data.dto

import java.io.Serializable

data class User(var username: String = "", var id: String = ""): Serializable {
    override fun toString(): String {
        return "User $username, id : $id"
    }
}