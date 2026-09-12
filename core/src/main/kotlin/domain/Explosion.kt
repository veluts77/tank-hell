package domain

import kotlin.math.roundToInt

class Explosion(
    private val centerX: Int,
    private val centerY: Int,
    private val radius: Int,
    speedFactor: Int,
    private val centerDamage: Int,
    private val edgeDamage: Int
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

    fun damageFor(area: Area): Int {
        val distance = area.distanceTo(centerX, centerY)
        if (distance > radius) return 0
        return (centerDamage - (centerDamage - edgeDamage) * (distance / radius)).roundToInt()
    }
}