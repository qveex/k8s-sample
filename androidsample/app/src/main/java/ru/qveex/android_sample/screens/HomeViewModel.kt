package ru.qveex.android_sample.screens

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.lastOrNull
import kotlinx.coroutines.launch
import ru.qveex.android_sample.api.createApi
import ru.qveex.android_sample.models.User

class HomeViewModel: ViewModel() {

    private val api = createApi()

    private val _users = MutableStateFlow<List<User>>(emptyList())
    val users = _users.asStateFlow()
    private val _error = MutableStateFlow<String?>(null)
    val error = _error.asStateFlow()

    init { getUsers() }

    private fun getUsers() = viewModelScope.launch {
        val response = api.getUsers()
        response.takeIf { it.isSuccessful }?.run {
            _users.value = this.body() ?: emptyList()
        } ?: run { _error.value = "can't get users: ${response.code()}" }
    }

    fun createUsers(
        name: String,
        age: Int
    ) = viewModelScope.launch {
        val response = api.createUser(User(name = name, age = age))
        if (!response.isSuccessful) {
            _error.value = "User not created: ${response.code()}"
        } else {
            getUsers()
        }
    }
}