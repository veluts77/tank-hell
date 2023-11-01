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

        val a = currentArea()
        g2.color = Color.red
        g2.fillOval(a.x, a.y, a.width, a.height)
    }

    fun createFallingBlockWidget(): FallingDustBlockWidget {
        val a = currentArea()
        return FallingDustBlockWidget(a.x, a.y, a.width, a.height)
    }

    private fun currentArea(): domain.Area {
        val currentRadius = explosion.currentRadius()
        val x = centerX - currentRadius
        val y = centerY - currentRadius
        val diameter = currentRadius * 2
        return domain.Area(x, y, diameter, diameter)
    }
}