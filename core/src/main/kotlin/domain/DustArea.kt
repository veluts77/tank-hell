package domain

import kotlin.math.sin

class DustArea {

    private val matrix: Array<BooleanArray> = Array(800) {
        BooleanArray(600) {
            false
        }
    }

    init {
        buildSineArea()
    }

    private fun buildSineArea() {
        for (x in 0..799) {
            for (y in 0..599) {
                if (y > 200 * sin(0.01 * x) + 300) matrix[x][y] = true
            }
        }
    }

    fun at(x: Int, y: Int): Boolean {
        return matrix[x][y]
    }
}