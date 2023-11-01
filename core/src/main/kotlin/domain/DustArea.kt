package domain

import kotlin.math.sin

class DustArea(
    private val width: Int,
    private val height: Int
) {

    private val matrix: Array<BooleanArray> = Array(width) {
        BooleanArray(height) {
            false
        }
    }

    init {
        buildSineArea()
    }

    private fun buildSineArea() {
        for (x in 0..<width) {
            for (y in 0..<height) {
                if (y > 200 * sin(0.01 * x) + 300) matrix[x][y] = true
            }
        }
    }

    fun at(x: Int, y: Int): Boolean {
        return matrix[x][y]
    }
}