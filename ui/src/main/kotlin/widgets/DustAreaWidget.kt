package widgets

import domain.DustArea
import java.awt.Color
import java.awt.Graphics2D

class DustAreaWidget {
    private val dustArea = DustArea()
    private val backgroundColor = Color(20, 55, 75, 255)

    fun draw(g2: Graphics2D) {
        g2.color = backgroundColor
        g2.fillRect(0, 0, 800, 600)
        g2.color = Color.blue
        for (y in 0..599) {
            for (x in 0..799) {
                if (dustArea.at(x, y)) {
                    g2.drawLine(x, y, x, y)
                }
            }
        }
    }
}