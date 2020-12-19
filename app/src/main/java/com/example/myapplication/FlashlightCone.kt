package com.example.myapplication

class FlashlightCone(viewWidth: Int, viewHeight: Int) {
    var x: Int
        private set
    var y: Int
        private set
    val radius: Int

    fun update(newX: Int, newY: Int) {
        x = newX
        y = newY
    }

    init {
        x = viewWidth / 2
        y = viewHeight / 2
        // Adjust the radius for the narrowest view dimension.
        radius = if (viewWidth <= viewHeight) x / 3 else y / 3
    }
}