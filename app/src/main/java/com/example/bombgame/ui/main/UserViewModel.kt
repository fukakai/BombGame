package com.example.bombgame.ui.main

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.bombgame.data.dto.User
import com.example.bombgame.repository.UserRepository
import kotlinx.coroutines.launch

class UserViewModel(private val userRepository: UserRepository) : ViewModel() {

    private val userLiveData = userRepository.getUserObserver()

    fun getUserObserver() = userLiveData

    fun addUser(user: User) {
        viewModelScope.launch {
            if (userRepository.getUser(user.id) == null) {
                userRepository.addUser(user)
            }
        }
        userRepository.listenToUser(user.id)
    }

    suspend fun getUser(id: String): User? {
        userRepository.listenToUser(id)
        return userRepository.getUser(id)
    }

    fun deleteUser(id: String) {
        userRepository.deleteUser(id)
    }

    fun updateUser(user: User, username: String) {
        if (user.id != "") {
            user.username = username
            userRepository.updateUser(user)
        }
    }
}