package ru.qveex.android_sample.models

import kotlinx.serialization.EncodeDefault
import kotlinx.serialization.Serializable

@Serializable
data class User(
    @EncodeDefault val id: Long = 0L,
    val name: String,
    val age: Int
)
