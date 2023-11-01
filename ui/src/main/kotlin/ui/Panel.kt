package ui

import widgets.DustAreaWidget
import widgets.FallingDustBlockWidget
import java.awt.Color
import java.awt.Dimension
import java.awt.Graphics
import java.awt.Graphics2D
import java.awt.event.MouseAdapter
import java.awt.event.MouseEvent
import java.awt.image.BufferedImage
import javax.swing.JPanel
import kotlin.math.max

class Panel: JPanel() {

    private val w = 800
    private val h = 600

    private val imgBuffer = BufferedImage(
        w, h, BufferedImage.TYPE_INT_RGB)

    private val blockWidgets = mutableListOf<FallingDustBlockWidget>()
    private val dustAreaWidget = DustAreaWidget(w, h)

    init {
        preferredSize = Dimension(w, h)

        addMouseListener(object : MouseAdapter() {
            override fun mousePressed(e: MouseEvent?) {
                if (e != null)
                    blockWidgets.add(FallingDustBlockWidget(e.x, e.y, 400, 400))
            }
        })
    }

    override fun paintComponent(g: Graphics?) {
        val beginTime = System.currentTimeMillis()

        drawScene(imgBuffer)
        processLogic()
        (g as Graphics2D).drawImage(imgBuffer, 0, 0, null)

        val elapsedTime = System.currentTimeMillis() - beginTime
        showTime(g, elapsedTime)

        try {
            Thread.sleep(max(25 - elapsedTime, 1))
        } catch (e: InterruptedException) {
            e.printStackTrace()
        }
    }

    private fun drawScene(image: BufferedImage) {
        val beginTime = System.currentTimeMillis()

        val g2 = image.createGraphics()
        dustAreaWidget.draw(g2)
        blockWidgets.forEach { it.draw(g2) }

        val endTime = System.currentTimeMillis()
        showInnerTime(g2, endTime - beginTime)
    }

    private fun processLogic() {
        val toRemove = HashSet<FallingDustBlockWidget>()
        blockWidgets.forEach {
            it.tick()
            if (it.completed()) toRemove.add(it)
        }
        blockWidgets.removeAll(toRemove)
    }

    private fun showTime(g: Graphics, time: Long) {
        g.color = Color.yellow
        g.drawString(time.toString(), 30, 62)
    }

    private fun showInnerTime(g: Graphics, time: Long) {
        g.color = Color.white
        g.drawString(time.toString(), 25, 45)
    }
}