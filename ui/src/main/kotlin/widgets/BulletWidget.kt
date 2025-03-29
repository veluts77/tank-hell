package widgets

import domain.Bullet
import domain.GameField
import java.awt.Color
import java.awt.Graphics2D

class BulletWidget(
    startX: Int,
    startY: Int,
    angle: Int,
    power: Int
) {
    private val bullet = Bullet(startX, startY, angle, power)

    fun tick() = bullet.tick()

    fun collided(gameField: GameField) = bullet.collided(gameField)

    fun collided(tankWidgets: List<TankWidget>): Boolean {
        val widget = tankWidgets.firstOrNull() { tank ->
            tank.area().intersects(bullet.area())
        }
        return widget != null
    }

    fun flownAwayFrom(gameField: GameField) = bullet.withinField(gameField).not()

    fun draw(g2: Graphics2D) {
        val a = bullet.area()

        g2.color = Color.red
        g2.fillOval(a.x, a.y, a.width, a.height)
    }

    fun explode() = ExplosionWidget(bullet.area().x, bullet.area().y, 50, 10)
}