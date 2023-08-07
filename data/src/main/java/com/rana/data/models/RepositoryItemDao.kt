package com.rana.data.models

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "RepositoryItemDao")
data class RepositoryItemDao(
    @PrimaryKey val uid: Int,
    @ColumnInfo(name = "name") val name: String = "",
    @ColumnInfo(name = "avatar") val avatar: String = "",
    @ColumnInfo(name = "score") val score: String = "0",
    @ColumnInfo(name = "url") val url: String = "",
    @ColumnInfo(name = "description") val description: String = "",
    @ColumnInfo(name = "language") val language:String = ""
)