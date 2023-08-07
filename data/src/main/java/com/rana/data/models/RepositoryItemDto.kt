package com.rana.data.models

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "RepositoryItemDto")
data class RepositoryItemDto(
    @PrimaryKey val uid: Int,
    @ColumnInfo(name = "name") val name: String,
    @ColumnInfo(name = "avatar") val avatar: String,
    @ColumnInfo(name = "score") val score: String,
    @ColumnInfo(name = "url") val url: String,
    @ColumnInfo(name = "description") val description: String,
    @ColumnInfo(name = "language") val language:String
)