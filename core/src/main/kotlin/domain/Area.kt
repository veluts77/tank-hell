package domain

import kotlin.math.hypot
import kotlin.math.max
import kotlin.math.min

class Area(
    val x: Int,
    val y: Int,
    val width: Int,
    val height: Int
) {
    fun intersects(area: Area): Boolean {
        return x < area.x + area.width &&
            x + width > area.x &&
            y < area.y + area.height &&
            y + height > area.y
    }

    fun distanceTo(px: Int, py: Int): Double {
        val closestX = min(max(px, x), x + width)
        val closestY = min(max(py, y), y + height)
        return hypot((px - closestX).toDouble(), (py - closestY).toDouble())
    }
}
