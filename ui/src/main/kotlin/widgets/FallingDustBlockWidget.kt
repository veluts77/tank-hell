package widgets

import FallingDustBlock
import java.awt.Color
import java.awt.Graphics2D

class FallingDustBlockWidget(
    var xPos: Int,
    var yPos: Int
) {
    val fallingDustBlock = FallingDustBlock()

    fun tick() = fallingDustBlock.tick()

    fun completed() = fallingDustBlock.completed()

    fun draw(g2: Graphics2D) {
        g2.color = Color.black
        g2.fillRect(xPos, yPos, 200, 200)
        g2.color = Color.blue
        for (y in 0..199) {
            for (x in 0..199) {
                if (fallingDustBlock.at(x, y)) {
                    g2.translate(xPos, yPos)
                    g2.drawLine(x, y, x, y)
                    g2.translate(-xPos, -yPos)
                }
            }
        }
    }
}