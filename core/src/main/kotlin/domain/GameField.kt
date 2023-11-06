package domain

import kotlin.math.sin

class GameField(
    private val width: Int,
    private val height: Int
) {

    private val matrix: Array<BooleanArray> = Array(width) {
        BooleanArray(height) {
            false
        }
    }

    init {
        buildSineField()
    }

    private fun buildSineField() {
        for (x in 0..<width) {
            for (y in 0..<height) {
                if (y > 200 * sin(0.01 * x) + 300) matrix[x][y] = true
            }
        }
    }

    fun at(x: Int, y: Int): Boolean {
        return matrix[x][y]
    }

    fun subMatrixFor(explosion: Explosion): Array<BooleanArray> {
        val area = explosion.currentExplosionArea()
        val x = area.x
        val y = findYofSubMatrixFor(area)
        val width = area.width
        val height = area.y - y + area.height
        return createAndFillSubMatrix(x, y, width, height)
    }

    private fun findYofSubMatrixFor(area: Area): Int {
        var result = area.y
        for (x in area.x..<(area.x + area.width)) {
            if (x < 0) continue
            for (y in 0..area.y) {
                if (matrix[x][y] && y < result) result = y
            }
        }
        return result
    }

    private fun createAndFillSubMatrix(x: Int, y: Int, width: Int, height: Int): Array<BooleanArray> {
        val subMatrix: Array<BooleanArray> = Array(width) { BooleanArray(height) }
        for (xx in 0..<width) {
            for (yy in 0..<height) {
                subMatrix[xx][yy] = matrix[xx + x][yy + y]
            }
        }
        return subMatrix
    }
}