package widgets

import domain.Cloud
import java.awt.Color
import java.awt.Graphics2D

object CloudWidget {
    fun draw(cloud: Cloud, g2: Graphics2D) {
        val baseColor = Color(30, 70, 100, 255)
        g2.color = baseColor

        for (blob in cloud.blobs()) {
            val cx = cloud.x() + blob.offsetX
            val cy = cloud.y() + blob.offsetY
            val r = blob.radius
            g2.fillOval(cx - r, cy - r, r * 2, r * 2)
        }
    }
}
