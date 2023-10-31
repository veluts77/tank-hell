package widgets

import domain.FallingDustBlock
import java.awt.Color
import java.awt.Graphics2D

class FallingDustBlockWidget(
    private val xPos: Int,
    private val yPos: Int,
    private val width: Int,
    private val height: Int
) {
    private val fallingDustBlock = FallingDustBlock(width, height)

    fun tick() = fallingDustBlock.tick()

    fun completed() = fallingDustBlock.completed()

    fun draw(g2: Graphics2D) {
        g2.color = Color.black
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