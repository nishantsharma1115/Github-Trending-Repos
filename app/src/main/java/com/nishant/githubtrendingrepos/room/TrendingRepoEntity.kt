package com.nishant.githubtrendingrepos.room

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "TrendingRepo")
data class TrendingRepoEntity(

    @PrimaryKey(autoGenerate = false)
    @ColumnInfo(name = "fullName")
    val fullName: String,

    @ColumnInfo(name = "startCount")
    val startCount: Int,

    @ColumnInfo(name = "language")
    val language: String?,

    @ColumnInfo(name = "description")
    val description: String?,

    @ColumnInfo(name = "forkCount")
    val forkCount: Int
)