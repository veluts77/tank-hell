package domain

class FallingDustBlock(
    private val matrix: Array<BooleanArray>
) {
    private val width = matrix.size
    private val height = matrix[0].size
    private val halfWidth = width / 2
    private var fallActionsCounter = 0

    init {
        cutCircularArea()
    }

    private fun cutCircularArea() {
        val radius = halfWidth
        val yCorrection = halfWidth + height - width
        for (x in 0..<radius)
            for (y in 0..<radius)
                if (x * x + y * y < radius * radius) {
                    tryReset(x + halfWidth, y + yCorrection)
                    tryReset(-x + halfWidth, y + yCorrection)
                    tryReset(x + halfWidth, -y + yCorrection)
                    tryReset(-x + halfWidth, -y + yCorrection)
                }
    }

    private fun tryReset(x: Int, y: Int): Boolean {
        try {
            matrix[x][y] = false
        } catch (e: ArrayIndexOutOfBoundsException) {
            return false
        }
        return true
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

    fun completed() = fallActionsCounter > width

    fun applyTo(gameField: GameField, xPos: Int, yPos: Int) {
        gameField.applySubMatrix(xPos, yPos, matrix)
    }
}