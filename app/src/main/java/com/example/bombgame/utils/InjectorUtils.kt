package com.example.bombgame.utils

import com.example.bombgame.repository.RoomRepository
import com.example.bombgame.repository.UserRepository
import com.example.bombgame.ui.main.RoomViewModelFactory
import com.example.bombgame.ui.main.UserViewModelFactory

object InjectorUtils {

    fun provideRoomViewModelFactory(): RoomViewModelFactory {
        val roomRepository = RoomRepository.getInstance()
        return RoomViewModelFactory(roomRepository)
    }

    fun provideUserViewModelFactory(): UserViewModelFactory {
        val userRepository = UserRepository.getInstance()
        return UserViewModelFactory(userRepository)
    }
}