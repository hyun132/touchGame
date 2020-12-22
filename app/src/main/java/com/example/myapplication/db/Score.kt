package com.example.myapplication.db

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.*

@Entity(tableName = "savedScore")
data class Score(
    @PrimaryKey( autoGenerate = true)
    @ColumnInfo(name = "score")val score:Int,
    val time: Int = Date().date
)