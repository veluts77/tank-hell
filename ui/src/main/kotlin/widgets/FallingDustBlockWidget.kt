package widgets

import domain.FallingDustBlock
import java.awt.Color
import java.awt.Graphics2D

class FallingDustBlockWidget(
    private val xPos: Int,
    private val yPos: Int,
    matrix: Array<BooleanArray>
) {
    private val width: Int = matrix.size
    private val height: Int = matrix[0].size
    private val fallingDustBlock = FallingDustBlock(matrix)
    private val backgroundColor = Color(20, 55, 75, 255)

    fun tick() = fallingDustBlock.tick()

    fun completed() = fallingDustBlock.completed()

    fun applyTo(gameFieldWidget: GameFieldWidget) {
        fallingDustBlock.applyTo(gameFieldWidget.gameField(), xPos, yPos)
    }

    fun draw(g2: Graphics2D) {
        g2.color = backgroundColor
        g2.fillRect(xPos, yPos, width, height)
        g2.color = Color.blue
        for (y in 0..<height) {
            for (x in 0..<width) {
                if (fallingDustBlock.at(x, y)) {
                    g2.translate(xPos, yPos)
                    g2.drawLine(x, y, x, y)
                    g2.translate(-xPos, -yPos)
                }
            }
        }
    }
}