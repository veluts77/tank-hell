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

    fun explode() = ExplosionWidget(xPos, yPos, 50, 10)

    fun area() = tank.area()
}