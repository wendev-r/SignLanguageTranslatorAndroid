package com.example.signlanguagetranslator.data

import com.example.signlanguagetranslator.util.Resource
import kotlinx.coroutines.flow.MutableSharedFlow

interface GloveSensorReceiveManager {

    val data: MutableSharedFlow<Resource<GloveResult>>

    fun reconnect()

    fun disconnect()

    fun startReceiving()

    fun closeConnection()

}