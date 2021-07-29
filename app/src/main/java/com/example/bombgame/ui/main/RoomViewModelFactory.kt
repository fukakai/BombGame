package com.example.bombgame.ui.main

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.bombgame.repository.RoomRepository

class RoomViewModelFactory(private val roomRepository: RoomRepository) :
    ViewModelProvider.NewInstanceFactory() {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return RoomViewModel(roomRepository) as T
    }
}