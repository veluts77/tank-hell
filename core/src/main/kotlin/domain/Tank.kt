package domain

class Tank(
    private var x: Int,
    private var y: Int,
) {

    private var falling = false

    fun tick() {
        if (falling) y += 10
    }

    // Todo: место для оптимизации
    fun area() = Area(x, y, 40, 20)

    fun startFalling() {
        falling = true
    }

    fun stopFalling() {
        falling = false
    }
}