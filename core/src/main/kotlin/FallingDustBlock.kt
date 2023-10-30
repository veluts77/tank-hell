import kotlin.math.sqrt

class FallingDustBlock {
    private var fallActionsCounter = 0

    private val matrix: Array<BooleanArray> = Array(200) {
        BooleanArray(200) {
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
        for (x in 0..99) {
            for (y in 0..99) {
                if (y > sqrt(10000.0 - x * x)) matrix[x + 100][y + 100] = true
            }
        }
    }

    private fun mirrorVertically() {
        for (x in 0..99) {
            for (y in 100..199) {
                matrix[x][y] = matrix[199 - x][y]
            }
        }
    }

    private fun mirrorHorizontally() {
        for (y in 0..99) {
            for (x in 0..199) {
                matrix[x][y] = matrix[x][199 - y]
            }
        }
    }

    fun tick() {
        for (i in 0..10) {
            fall()
        }
    }

    private fun fall() {
        for (y in 199 downTo 1) {
            for (x in 0..199) {
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

    fun completed() = fallActionsCounter > 200
}