package ui

import java.awt.Color

object TankPalette {
    const val TANK_WIDTH = 40
    const val SPAWN_Y = 50
    const val FIELD_WIDTH = 800
    const val PADDING = 40

    val COLORS = listOf(
        Color.orange,
        Color.gray,
        Color.magenta,
        Color.cyan,
        Color.blue,
        Color.pink
    )

    private val NAMES = listOf("Orange", "Gray", "Magenta", "Cyan", "Blue", "Pink")

    fun displayName(color: Color): String {
        val index = COLORS.indexOf(color)
        return if (index >= 0) NAMES[index] else "Unknown"
    }

    fun spawnX(index: Int, playerCount: Int): Int {
        val slot = (FIELD_WIDTH - 2 * PADDING) / playerCount
        return PADDING + index * slot + (slot - TANK_WIDTH) / 2
    }
}
