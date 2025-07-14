package com.example.signlanguagetranslator.data

data class GloveResult(
    /*
    TODO: Temp values for the IMU data - Update these values when we know the exact values that get collected from the sensors and passed through
     */
    val x: Float,
    val y: Float,
    val z: Float,
    val connectionState: ConnectionState
)
