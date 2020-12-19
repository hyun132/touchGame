package com.example.myapplication

class MyPlane(x: Int, y: Int, speed:Int) {
    var speed=speed
    var planeX=x
    var planeY=y

    fun move(){
        planeY+=speed
    }
}