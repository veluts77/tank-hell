package domain

class Explosion(
    private val centerX: Int,
    private val centerY: Int,
    private val radius: Int,
    speedFactor: Int
) {
    private var currentRadius = 0
    private val increment = radius / speedFactor

    fun tick() {
        currentRadius += increment
    }

    fun completed() = currentRadius >= radius

    fun currentExplosionArea(): Area {
        val currentRadius = currentRadius
        val x = centerX - currentRadius
        val y = centerY - currentRadius
        val diameter = currentRadius * 2
        return Area(x, y, diameter, diameter)
    }
}