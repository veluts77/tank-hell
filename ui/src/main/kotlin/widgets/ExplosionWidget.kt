package widgets

import domain.GameField
import domain.Explosion
import java.awt.Color
import java.awt.Graphics2D

class ExplosionWidget(
    centerX: Int,
    centerY: Int,
    radius: Int,
    speedFactor: Int
) {
    private val explosion = Explosion(centerX, centerY, radius, speedFactor)

    fun tick() = explosion.tick()

    fun completed() = explosion.completed()

    fun draw(g2: Graphics2D) {
        val a = explosion.currentExplosionArea()
        if (a.width <= 0) return

        g2.color = Color.red
        g2.fillOval(a.x, a.y, a.width, a.height)
    }

    fun createFallingBlockWidgetOn(gameField: GameField): FallingDustBlockWidget {
        val fallingBlockMatrix = gameField.subMatrixFor(explosion)
        val a = explosion.currentExplosionArea()
        return FallingDustBlockWidget(a.x, a.y - (fallingBlockMatrix[0].size - a.height), fallingBlockMatrix)
    }
}