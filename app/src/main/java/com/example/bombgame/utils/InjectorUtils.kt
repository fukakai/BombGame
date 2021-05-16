package com.example.bombgame.utils

import com.example.bombgame.repository.RoomRepository
import com.example.bombgame.ui.main.MainViewModelFactory

object InjectorUtils {

    fun provideMainViewModelFactory(): MainViewModelFactory {
        val roomRepository = RoomRepository.getInstance()
        return MainViewModelFactory(roomRepository)
    }
}