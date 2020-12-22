package com.example.myapplication.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface ScoreDao {
    @Query("Select * FROM savedScore ORDER by score DESC")
    fun getAllScores():List<Score>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun saveScore(score:Score)
}