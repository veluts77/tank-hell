package domain

import kotlin.math.cos
import kotlin.math.roundToInt
import kotlin.math.sin

class Bullet (
    private val startX: Int,
    private val startY: Int,
    angle: Int,
    private val power: Int
) {
    private var x = 0.0
    private var y = 0.0
    private var t = 0.0
    private val cosa = cos(angle.toDouble())
    private val sina = sin(angle.toDouble())

    init {
        x = startX.toDouble()
        y = startY.toDouble()
    }

    // Todo: место для оптимизации
    fun area() = Area(x.roundToInt(), y.roundToInt(), 6, 6)

    fun withinField(gameField: GameField) = gameField.withinField(x.roundToInt(), y.roundToInt())

    fun collided(gameField: GameField) = gameField.at(x.roundToInt(), y.roundToInt())

    fun tick() {
        t += 1
        x = startX + power * t * cosa
        y = startY - power * t * sina + 0.1 * t * t
    }
}