package domain

class Area(
    val x: Int,
    val y: Int,
    val width: Int,
    val height: Int
) {
    fun intersects(area: Area): Boolean {
        return false // Todo
    }
}