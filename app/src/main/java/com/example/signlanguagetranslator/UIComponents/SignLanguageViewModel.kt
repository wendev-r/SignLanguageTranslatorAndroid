package com.example.signlanguagetranslator.UIComponents

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.signlanguagetranslator.data.ConnectionState
import com.example.signlanguagetranslator.data.GloveSensorReceiveManager
import com.example.signlanguagetranslator.util.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

//@HiltViewModel
//class SignLanguageViewModel @Inject constructor(
//    private val gloveSensorReceiveManager: GloveSensorReceiveManager
//) : ViewModel() {
//
//    var initializingMessage by mutableStateOf<String?>(null)
//        private set
//    var errorMessage by mutableStateOf<String?>(null)
//        private set
//
//    var pointX by mutableFloatStateOf(0f)
//        private set
//
//    var pointY by mutableFloatStateOf(0f)
//        private set
//
//    var pointZ by mutableFloatStateOf(0f)
//        private set
//
//    var connectionState by mutableStateOf<ConnectionState>(ConnectionState.Uninitialized)
//        private set
//
//
//    private fun subscribeToChanges() {
//        viewModelScope.launch {
//            gloveSensorReceiveManager.data.collect { result ->
//                when (result) {
//                    is Resource.Success -> {
//                        connectionState = result.data.connectionState
//                        pointX = result.data.x
//                        pointY = result.data.y
//                        pointZ = result.data.z
//
//                    }
//
//                    is Resource.Loading -> {
//                        initializingMessage = result.message
//                        connectionState = ConnectionState.CurrentlyInitializing
//                    }
//
//                    is Resource.Error -> {
//                        errorMessage = result.errorMessage
//                        connectionState = ConnectionState.Uninitialized
//                    }
//
//                }
//            }
//        }
//    }
//
//    fun disconnect() {
//        gloveSensorReceiveManager.disconnect()
//    }
//
//    fun reconnect() {
//        gloveSensorReceiveManager.reconnect()
//    }
//
//    fun initializeConnection() {
//        errorMessage = null
//        subscribeToChanges()
//        gloveSensorReceiveManager.startReceiving()
//    }
//
//    override fun onCleared() {
//        super.onCleared()
//        gloveSensorReceiveManager.closeConnection()
//    }
//
//
//}
@HiltViewModel
class SignLanguageViewModel @Inject constructor(
    private val gloveSensorReceiveManager: GloveSensorReceiveManager
) : ViewModel() {

    var initializingMessage by mutableStateOf<String?>(null)
        private set

    var errorMessage by mutableStateOf<String?>(null)
        private set

    var pointX by mutableFloatStateOf(0f)
        private set

    var pointY by mutableFloatStateOf(0f)
        private set

    var pointZ by mutableFloatStateOf(0f)
        private set

    var connectionState by mutableStateOf<ConnectionState>(ConnectionState.Uninitialized)

    private fun subscribeToChanges() {
        viewModelScope.launch {
            gloveSensorReceiveManager.data.collect { result ->
                when (result) {
                    is Resource.Success -> {
                        connectionState = result.data.connectionState
                        pointX = result.data.x
                        pointY = result.data.y
                        pointZ = result.data.z
                    }

                    is Resource.Loading -> {
                        initializingMessage = result.message
                        connectionState = ConnectionState.CurrentlyInitializing
                    }

                    is Resource.Error -> {
                        errorMessage = result.errorMessage
                        connectionState = ConnectionState.Uninitialized
                    }
                }
            }
        }
    }

    fun disconnect() {
        gloveSensorReceiveManager.disconnect()
    }

    fun reconnect() {
        gloveSensorReceiveManager.reconnect()
    }

    fun initializeConnection() {
        errorMessage = null
        subscribeToChanges()
        gloveSensorReceiveManager.startReceiving()
    }

    override fun onCleared() {
        super.onCleared()
        gloveSensorReceiveManager.closeConnection()
    }


}