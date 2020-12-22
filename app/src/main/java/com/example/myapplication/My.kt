//package com.example.myapplication
//
//import android.app.Application
//import android.util.Log
//import androidx.lifecycle.LiveData
//import com.example.myapplication.db.Score
//import com.example.myapplication.db.ScoreDB
//
//class MyApplication:Application() {
//    lateinit var isOver:LiveData<Score>
//    val db = ScoreDB.getDB(this)
//    fun getDb(){
//        db.scoreDao().saveScore(Score(score = curscore))
//        var tempList= db.scoreDao().getAllScores()
//        rankList=tempList.subList(0,5)
//        Log.d("load db", rankList[0].toString())
//    }
//}