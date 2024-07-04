package domain

import kotlin.math.sin

/**
 *  Game field with (0,0) corresponding to top left corner.
 */
class GameField(
    private val width: Int,
    private val height: Int
) {

    /**
     * Represents hill-like structure where 'false' is white space and 'true' is dirt
     */
    private val matrix: Array<BooleanArray> = Array(width) {
        BooleanArray(height) {
            false
        }
    }

    init {
        buildSineField()
    }

    /**
     * Initial shape of the game field, simulating some sort of hill landscape
     */
    private fun buildSineField() {
        for (x in 0..<width) {
            for (y in 0..<height) {
                if (y > 200 * sin(0.01 * x) + 300) matrix[x][y] = true
            }
        }
    }

    /**
     * Find if there is a dirt (true) of white space (false) at the specific point provided
     */
    fun at(x: Int, y: Int): Boolean {
        return matrix[x][y]
    }

    /**
     * Returns a matrix representing a shape of an explosion
     */
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
        val matrixWidth = matrix.size
        for (x in area.x..<(area.x + area.width)) {
            if (x < 0 || x >= matrixWidth) continue
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
                subMatrix[xx][yy] = matrixValueOrFalse(xx + x, yy + y)
            }
        }
        return subMatrix
    }

    private fun matrixValueOrFalse(x: Int, y: Int): Boolean {
        return try {
            matrix[x][y]
        } catch (e: ArrayIndexOutOfBoundsException) {
            false
        }
    }

    /**
     * Apply provided (sub)matrix at the specific position of Game field.
     * The method if boundary safe, meaning it causes no ArrayIndexOutOfBoundsException.
     */
    fun applySubMatrix(xPos: Int, yPos: Int, subMatrix: Array<BooleanArray>) {
        for (x in subMatrix.indices) {
            val matrixX = x + xPos
            if (matrixX < 0 || matrixX >= width) continue
            for (y in subMatrix[0].indices) {
                val matrixY = y + yPos
                if (matrixY < 0 || matrixY >= height) continue
                matrix[matrixX][matrixY] = subMatrix[x][y]
            }
        }
    }
}