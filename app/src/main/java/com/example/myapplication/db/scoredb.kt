package com.example.myapplication.db

import android.app.Application
import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = arrayOf(Score::class),version = 1)
abstract class ScoreDB:RoomDatabase(){
    abstract fun scoreDao():ScoreDao

    companion object{
        private var DB_INSTANCE:ScoreDB?=null
        fun getDB(context: Context):ScoreDB{
            val tempInstance= DB_INSTANCE
            if(tempInstance!=null){
                return tempInstance
            }
            synchronized(this){
                val instance = Room.databaseBuilder(context.applicationContext,ScoreDB::class.java,"savedScore").build()
                DB_INSTANCE=instance
                return instance
            }
        }
    }
}


