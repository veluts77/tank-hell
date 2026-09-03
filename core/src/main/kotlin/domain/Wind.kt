package domain

enum class WindDirection {
    LEFT, RIGHT
}

class Wind private constructor(
    private val strength: Int,
    private val direction: WindDirection
) {
    fun signedStrength(): Int = when (direction) {
        WindDirection.LEFT -> -strength
        WindDirection.RIGHT -> strength
    }

    fun displayText(): String = when {
        strength == 0 -> "Wind: 0"
        direction == WindDirection.LEFT -> "Wind: ← $strength"
        else -> "Wind: $strength →"
    }

    companion object {
        const val MIN_STRENGTH = 0
        const val MAX_STRENGTH = 10

        fun random(): Wind = Wind(
            strength = (MIN_STRENGTH..MAX_STRENGTH).random(),
            direction = WindDirection.entries.random()
        )
    }
}
