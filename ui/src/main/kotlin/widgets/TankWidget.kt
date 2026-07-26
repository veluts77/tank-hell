package widgets

import domain.Tank
import java.awt.Color
import java.awt.Graphics2D

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

        g2.color = color
        g2.fillRect(a.x, a.y, a.width, a.height)
    }

    fun explode(): ExplosionWidget {
        val a = tank.area()
        return ExplosionWidget(a.x + a.width / 2, a.y + a.height / 2, 50, 10)
    }

    fun area() = tank.area()

    fun health() = tank.health()

    fun applyDamage(amount: Int) = tank.applyDamage(amount)

    fun isDestroyed() = tank.isDestroyed()

    fun muzzlePoint() = tank.muzzlePoint()

    fun aimAngleDegrees() = tank.aimAngleDegrees()

    fun power() = tank.power()

    fun adjustAimAngle(delta: Int) = tank.adjustAimAngle(delta)

    fun adjustPower(delta: Int) = tank.adjustPower(delta)

    //TODO подумать над альтернативой такому количеству прокси функций и использовать tank()
}
