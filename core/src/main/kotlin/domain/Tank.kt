package domain

class Tank(
    x: Int,
    y: Int,
) {
    private val area = Area(x, y, 40, 20)

    fun tick() {
        //TODO
    }

    fun completed() = false

    fun area() = area
}