package domain

class Tank(
    private var x: Int,
    private var y: Int,
) {

    private var falling = false
    private var aimAngleDegrees = 45
    private var power = 10

    fun tick() {
        if (falling) y += 10
    }

    // Todo: место для оптимизации
    fun area() = Area(x, y, 40, 20)

    fun muzzlePoint(): Pair<Int, Int> {
        val a = area()
        return Pair(a.x + a.width / 2, a.y)
    }

    fun aimAngleDegrees() = aimAngleDegrees

    fun power() = power

    fun adjustAimAngle(delta: Int) {
        aimAngleDegrees = (aimAngleDegrees + delta).coerceIn(MIN_ANGLE, MAX_ANGLE)
    }

    fun adjustPower(delta: Int) {
        power = (power + delta).coerceIn(MIN_POWER, MAX_POWER)
    }

    fun startFalling() {
        falling = true
    }

    fun stopFalling() {
        falling = false
    }

    companion object {
        const val MIN_ANGLE = 0
        const val MAX_ANGLE = 180
        const val MIN_POWER = 1
        const val MAX_POWER = 20
    }
}
