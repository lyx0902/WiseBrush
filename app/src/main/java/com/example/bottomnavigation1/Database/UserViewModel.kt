//package com.example.bottomnavigation1.Database
//
//import android.app.Application
//import androidx.lifecycle.*
//import kotlinx.coroutines.launch
//
//class UserViewModel(application: Application) : AndroidViewModel(application) {
//
//    private val repository: UserRepository
//    val allUsers: LiveData<List<User>>
//
//    init {
//        val userDao = AppDatabase.getDatabase(application).userDao()
//        repository = UserRepository(userDao)
//        allUsers = repository.allUsers.asLiveData()
//    }
//
//    fun insert(user: User) = viewModelScope.launch {
//        repository.insert(user)
//    }
//}