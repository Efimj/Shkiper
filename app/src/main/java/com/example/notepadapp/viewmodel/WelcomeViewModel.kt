package com.example.notepadapp.viewmodel

//import androidx.lifecycle.ViewModel
//import androidx.lifecycle.viewModelScope
//import com.example.notepadapp.data.DataStoreRepository
//import dagger.hilt.android.lifecycle.HiltViewModel
//import kotlinx.coroutines.Dispatchers
//import kotlinx.coroutines.launch
//import javax.inject.Inject
//
//@HiltViewModel
//class WelcomeViewModel @Inject constructor(
//    private val repository: DataStoreRepository
//) : ViewModel() {
//
//    fun saveOnBoardingState(completed: Boolean) {
//        viewModelScope.launch(Dispatchers.IO) {
//            repository.saveOnBoardingState(completed = completed)
//        }
//    }
//}
