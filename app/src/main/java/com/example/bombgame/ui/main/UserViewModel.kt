package com.example.bombgame.ui.main

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.bombgame.data.dto.User
import com.example.bombgame.repository.UserRepository
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import kotlin.coroutines.EmptyCoroutineContext

class UserViewModel(private val userRepository: UserRepository) : ViewModel() {

    private val userLiveData = userRepository.getUserObserver()

    fun getUserObserver() = userLiveData

    suspend fun saveUser(): User {
        val user = User()
        user.id = FirebaseAuth.getInstance().currentUser?.uid.toString()
        CoroutineScope(EmptyCoroutineContext).launch {
            user.username = getUser(user.id)?.username.toString()
        }
        addUser(user)
        return user
    }

    private fun addUser(user: User) {
        viewModelScope.launch {
            if (userRepository.getUser(user.id) == null) {
                userRepository.addUser(user)
            }
        }
        userRepository.listenToUser(user.id)
    }

    private suspend fun getUser(id: String): User? {
        userRepository.listenToUser(id)
        return userRepository.getUser(id)
    }

    fun deleteUser(id: String) {
        userRepository.deleteUser(id)
    }

    fun updateUser(user: User, username: String) {
        user.username = username
        userRepository.updateUser(user)
    }
}