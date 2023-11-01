package domain

class Explosion(
    private val radius: Int,
    speedFactor: Int
) {
    private var currentRadius = 0
    private val increment = radius / speedFactor

    fun tick() {
        currentRadius += increment
    }

    fun completed() = currentRadius >= radius

    fun currentRadius() = currentRadius
}