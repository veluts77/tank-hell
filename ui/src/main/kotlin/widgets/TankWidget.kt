package widgets

import domain.Tank
import java.awt.BasicStroke
import java.awt.Color
import java.awt.Graphics2D
import java.awt.RenderingHints
import kotlin.math.cos
import kotlin.math.roundToInt
import kotlin.math.sin

class TankWidget(
    private val xPos: Int,
    private val yPos: Int,
    private val color: Color
) {
    private val tank = Tank(xPos, yPos)

    fun tick() = tank.tick()

    fun startFalling() = tank.startFalling()

    fun stopFalling() = tank.stopFalling()

    fun draw(g2: Graphics2D) {
        val a = tank.area()
        val oldStroke = g2.stroke
        val oldAntialiasing = g2.getRenderingHint(RenderingHints.KEY_ANTIALIASING)
        val outlineColor = color.darker()

        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON)

        g2.color = outlineColor
        g2.fillRoundRect(a.x, a.y + TRACK_Y_OFFSET, a.width, TRACK_HEIGHT, TRACK_ARC, TRACK_ARC)

        g2.color = color
        g2.fillRoundRect(a.x + HULL_INSET, a.y + HULL_Y_OFFSET, a.width - HULL_INSET * 2, HULL_HEIGHT, HULL_ARC, HULL_ARC)
        g2.color = outlineColor
        g2.stroke = BasicStroke(1f)
        g2.drawRoundRect(a.x + HULL_INSET, a.y + HULL_Y_OFFSET, a.width - HULL_INSET * 2, HULL_HEIGHT, HULL_ARC, HULL_ARC)

        val (cx, cy) = turretCenter()
        val (endX, endY) = barrelEnd()
        g2.color = color
        g2.stroke = BasicStroke(BARREL_THICKNESS, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND)
        g2.drawLine(cx.roundToInt(), cy.roundToInt(), endX.roundToInt(), endY.roundToInt())

        val turretX = a.x + (a.width - TURRET_WIDTH) / 2
        val turretY = a.y + TURRET_Y_OFFSET
        g2.color = color
        g2.fillRoundRect(turretX, turretY, TURRET_WIDTH, TURRET_HEIGHT, TURRET_ARC, TURRET_ARC)
        g2.color = outlineColor
        g2.stroke = BasicStroke(1f)
        g2.drawRoundRect(turretX, turretY, TURRET_WIDTH, TURRET_HEIGHT, TURRET_ARC, TURRET_ARC)

        g2.stroke = oldStroke
        if (oldAntialiasing != null) {
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, oldAntialiasing)
        }
    }

    fun explode(): ExplosionWidget {
        val a = tank.area()
        return ExplosionWidget(a.x + a.width / 2, a.y + a.height / 2, 50, 10)
    }

    fun area() = tank.area()

    fun health() = tank.health()

    fun applyDamage(amount: Int) = tank.applyDamage(amount)

    fun isDestroyed() = tank.isDestroyed()

    fun muzzlePoint(): Pair<Int, Int> {
        val (endX, endY) = barrelEnd()
        return Pair(endX.roundToInt(), endY.roundToInt())
    }

    fun aimAngleDegrees() = tank.aimAngleDegrees()

    fun power() = tank.power()

    fun adjustAimAngle(delta: Int) = tank.adjustAimAngle(delta)

    fun adjustPower(delta: Int) = tank.adjustPower(delta)

    private fun turretCenter(): Pair<Double, Double> {
        val a = tank.area()
        return Pair(a.x + a.width / 2.0, a.y + TURRET_CENTER_Y)
    }

    private fun barrelEnd(): Pair<Double, Double> {
        val (cx, cy) = turretCenter()
        val radians = Math.toRadians(tank.aimAngleDegrees().toDouble())
        return Pair(
            cx + BARREL_LENGTH * cos(radians),
            cy - BARREL_LENGTH * sin(radians)
        )
    }

    //TODO подумать над альтернативой такому количеству прокси функций и использовать tank()

    companion object {
        private const val TRACK_Y_OFFSET = 14
        private const val TRACK_HEIGHT = 6
        private const val TRACK_ARC = 3
        private const val HULL_INSET = 2
        private const val HULL_Y_OFFSET = 6
        private const val HULL_HEIGHT = 12
        private const val HULL_ARC = 6
        private const val TURRET_WIDTH = 16
        private const val TURRET_HEIGHT = 12
        private const val TURRET_Y_OFFSET = 2
        private const val TURRET_ARC = 8
        private const val TURRET_CENTER_Y = TURRET_Y_OFFSET + TURRET_HEIGHT / 2.0
        private const val BARREL_LENGTH = 18.0
        private const val BARREL_THICKNESS = 4f
    }
}
