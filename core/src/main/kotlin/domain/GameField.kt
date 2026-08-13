package domain

import kotlin.math.PI
import kotlin.math.cos
import kotlin.random.Random

/**
 *  Game field with (0,0) corresponding to top left corner.
 */
class GameField(
    private val width: Int,
    private val height: Int,
    private val random: Random = Random.Default
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
        buildLandscape()
    }

    /**
     * Random natural landscape: plains interrupted by hills and mountain ranges.
     * Peaks leave the top quarter of the field as sky; valleys stay above the bottom 10%.
     */
    private fun buildLandscape() {
        val minSurfaceY = (height * TOP_SKY_FRACTION).toInt()
        val maxSurfaceY = (height * (1.0 - BOTTOM_GROUND_FRACTION)).toInt()
        val yRange = maxSurfaceY - minSurfaceY
        val elevation = generateElevation()
        for (x in 0..<width) {
            val surfaceY = maxSurfaceY - (elevation[x] * yRange).toInt()
            for (y in (surfaceY + 1)..<height) {
                matrix[x][y] = true
            }
        }
    }

    /**
     * 1D elevation in 0..1 (1 = peak). Mixes rolling plains/hills everywhere
     * with larger mountains only where a low-frequency mask is high.
     */
    private fun generateElevation(): DoubleArray {
        val plains = valueNoise((width / 2).coerceAtLeast(2))
        val hills = valueNoise((width / 4).coerceAtLeast(2))
        val mountains = valueNoise((width / 10).coerceAtLeast(2))
        val detail = valueNoise(DETAIL_WAVELENGTH.coerceIn(2, (width / 2).coerceAtLeast(2)))
        val maskNoise = valueNoise((width * 2 / 3).coerceAtLeast(2))

        val combined = DoubleArray(width) { x ->
            val mountainMask = smoothstep(MASK_EDGE0, MASK_EDGE1, maskNoise[x])
            plains[x] * PLAINS_AMP +
                hills[x] * HILLS_AMP +
                (mountains[x] * MOUNTAINS_AMP + detail[x] * DETAIL_AMP) * mountainMask
        }
        return normalize(combined)
    }

    private fun valueNoise(wavelength: Int): DoubleArray {
        val step = wavelength.coerceAtLeast(1)
        val lattice = DoubleArray(width / step + 2) { random.nextDouble() }
        return DoubleArray(width) { x ->
            val index = (x / step).coerceAtMost(lattice.size - 2)
            val t = (x % step).toDouble() / step
            cosineInterpolate(lattice[index], lattice[index + 1], t)
        }
    }

    private fun cosineInterpolate(a: Double, b: Double, t: Double): Double {
        val f = (1 - cos(t * PI)) * 0.5
        return a * (1 - f) + b * f
    }

    private fun smoothstep(edge0: Double, edge1: Double, x: Double): Double {
        val t = ((x - edge0) / (edge1 - edge0)).coerceIn(0.0, 1.0)
        return t * t * (3 - 2 * t)
    }

    private fun normalize(values: DoubleArray): DoubleArray {
        var min = values[0]
        var max = values[0]
        for (v in values) {
            if (v < min) min = v
            if (v > max) max = v
        }
        val span = (max - min).coerceAtLeast(1e-9)
        return DoubleArray(values.size) { i -> (values[i] - min) / span }
    }

    /**
     * Find if there is a dirt (true) of white space (false) at the specific point provided
     */
    fun at(x: Int, y: Int): Boolean {
        return matrix[x][y]
    }

    fun withinField(x: Int, y: Int): Boolean {
        return x >= 0 && y >= 0 && x < width && y < height
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

    companion object {
        private const val TOP_SKY_FRACTION = 0.30
        private const val BOTTOM_GROUND_FRACTION = 0.15
        private const val DETAIL_WAVELENGTH = 30
        private const val PLAINS_AMP = 0.15
        private const val HILLS_AMP = 0.25
        private const val MOUNTAINS_AMP = 0.4
        private const val DETAIL_AMP = 0.08
        private const val MASK_EDGE0 = 0.4
        private const val MASK_EDGE1 = 0.8
    }
}