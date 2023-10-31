package domain

import kotlin.math.sqrt

class FallingDustBlock(
    private val width: Int,
    private val height: Int
) {
    private val halfWidth = width / 2
    private val halfHeight = height / 2
    private var fallActionsCounter = 0

    private val matrix: Array<BooleanArray> = Array(width) {
        BooleanArray(height) {
            false
        }
    }

    init {
        buildCircularArea()
    }

    private fun buildCircularArea() {
        buildOneSector()
        mirrorVertically()
        mirrorHorizontally()
    }

    private fun buildOneSector() {
        val squaredHalfWidth = halfWidth * halfWidth.toDouble()
        for (x in 0..<halfWidth) {
            for (y in 0..<halfHeight) {
                if (y > sqrt(squaredHalfWidth - x * x)) {
                    matrix[x + halfWidth][y + halfHeight] = true
                }
            }
        }
    }

    private fun mirrorVertically() {
        for (x in 0..<halfWidth) {
            for (y in halfHeight..<height) {
                matrix[x][y] = matrix[width - 1 - x][y]
            }
        }
    }

    private fun mirrorHorizontally() {
        for (y in 0..<halfHeight) {
            for (x in 0..<width) {
                matrix[x][y] = matrix[x][height - 1 - y]
            }
        }
    }

    fun tick() {
        for (i in 0..10) {
            fall()
        }
    }

    private fun fall() {
        for (y in height - 1 downTo 1) {
            for (x in 0..<width) {
                if (matrix[x][y].not()) {
                    if (matrix[x][y - 1]) {
                        matrix[x][y] = true
                        matrix[x][y - 1] = false
                    }
                }
            }
        }
        fallActionsCounter ++
    }

    fun at(x: Int, y: Int): Boolean {
        return matrix[x][y]
    }

    fun completed() = fallActionsCounter > height
}