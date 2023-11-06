package widgets

import domain.GameField
import java.awt.Color
import java.awt.Graphics2D

class GameFieldWidget(
    private val width: Int,
    private val height: Int
) {
    private val gameField = GameField(width, height)
    private val backgroundColor = Color(20, 55, 75, 255)

    fun draw(g2: Graphics2D) {
        g2.color = backgroundColor
        g2.fillRect(0, 0, width, height)
        g2.color = Color.blue
        for (x in 0..<width) {
            for (y in 0..<height) {
                if (gameField.at(x, y)) {
                    g2.drawLine(x, y, x, height - 1)
                    break
                }
            }
        }
    }

    fun gameField() = gameField
}