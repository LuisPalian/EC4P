package com.pailan.ec4.model

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize

@Entity(tableName = "api")
@Parcelize
data class Api(
    @PrimaryKey
    val uuid: String,
    val displayName: String,
    val description: String,
    val developerName: String,
    val displayIcon: String,
    var isFavorite: Boolean = false
    ):Parcelable
fun getData(): List<Api> =

    listOf(
        Api("", "Nike cupon", "Nike Store", "50", "Obten un 50% de descuento por dia del padre.",false)

        )