package widgets

import domain.Explosion
import java.awt.Color
import java.awt.Graphics2D

class ExplosionWidget(
    private val centerX: Int,
    private val centerY: Int,
    radius: Int,
    speedFactor: Int
) {
    private val explosion = Explosion(radius, speedFactor)

    fun tick() = explosion.tick()

    fun completed() = explosion.completed()

    fun draw(g2: Graphics2D) {
        val currentRadius = explosion.currentRadius()
        if (currentRadius <= 0) return

        val x = centerX - currentRadius
        val y = centerY - currentRadius
        val diameter = currentRadius * 2
        g2.color = Color.red
        g2.fillOval(x, y, diameter, diameter)
    }
}