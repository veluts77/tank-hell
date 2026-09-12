package widgets

import domain.Bullet
import domain.GameField
import domain.WeaponSpec
import domain.WeaponType
import domain.Wind
import java.awt.Color
import java.awt.Graphics2D

class BulletWidget(
    startX: Int,
    startY: Int,
    angle: Int,
    power: Int,
    wind: Wind,
    private val owner: TankWidget,
    private val spec: WeaponSpec = WeaponSpec.STANDARD
) {
    private val bullet = Bullet(startX, startY, angle, power, wind)
    private var leftOwner = false

    fun tick() = bullet.tick()

    fun collided(gameField: GameField) = bullet.collided(gameField)

    fun collided(tankWidgets: List<TankWidget>): Boolean {
        val bulletArea = bullet.area()
        if (!leftOwner) {
            if (bulletArea.intersects(owner.area())) return false
            leftOwner = true
        }
        return tankWidgets.any { widget ->
            widget.area().intersects(bulletArea)
        }
    }

    fun flownAwayFrom(gameField: GameField) = bullet.withinField(gameField).not()

    fun draw(g2: Graphics2D) {
        val a = bullet.area()

        g2.color = bulletColor()
        g2.fillOval(a.x, a.y, a.width, a.height)
    }

    fun explode(): ExplosionWidget {
        val a = bullet.area()
        return ExplosionWidget(
            a.x + a.width / 2,
            a.y + a.height / 2,
            spec.explosionRadius,
            10,
            owner.playerIndex(),
            spec.centerDamage,
            spec.edgeDamage
        )
    }

    private fun bulletColor() = when (spec.type) {
        WeaponType.CLEANER -> Color.cyan
        WeaponType.HEAVY -> Color.yellow
        else -> Color.red
    }
}
