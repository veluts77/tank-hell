class FallingDustBlock {
    private var fallActionsCounter = 0

    private val matrix: Array<BooleanArray> = Array(200) {
        BooleanArray(200) {
            false
        }
    }

    init {
        diagonal()
    }

    private fun diagonal() {
        for (x in 0..199) {
            for (y in 0..199) {
                if (x > y) matrix[x][y] = true
            }
        }
    }

    fun tick() {
        for (i in 0..4) {
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