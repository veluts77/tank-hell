package ui

import widgets.FallingDustBlockWidget
import java.awt.Color
import java.awt.Dimension
import java.awt.Graphics
import java.awt.Graphics2D
import java.awt.RenderingHints.KEY_ANTIALIASING
import java.awt.RenderingHints.VALUE_ANTIALIAS_ON
import java.awt.event.MouseAdapter
import java.awt.event.MouseEvent
import java.awt.image.BufferedImage
import javax.swing.ImageIcon
import javax.swing.JFrame
import javax.swing.JLabel

class Form : JFrame(), Runnable {

    private val backgroundColor = Color(20, 55, 75, 255)
    private val w = 800
    private val h = 600

    private val imgBuffer = BufferedImage(
        w, h, BufferedImage.TYPE_INT_RGB)

    private var blockWidgets = mutableListOf<FallingDustBlockWidget>()

    init {
        isVisible = true
        defaultCloseOperation = EXIT_ON_CLOSE
        setLocation(50, 50)
        val canvas = JLabel(ImageIcon(imgBuffer))
        canvas.preferredSize = Dimension(w, h)
        add(canvas)
        pack()

        canvas.addMouseListener(object : MouseAdapter() {
            override fun mousePressed(e: MouseEvent?) {
                if (e != null)
                    blockWidgets.add(FallingDustBlockWidget(e.x, e.y))
            }
        })
    }


    override fun run() {
        while (true) repaint()
    }

    override fun paint(g: Graphics?) {
        val beginTime = System.currentTimeMillis()

        drawScene(imgBuffer)
        processLogic()
        (g as Graphics2D).drawImage(imgBuffer, null, 8, 30)

        val elapsedTime = System.currentTimeMillis() - beginTime
        showTime(g, elapsedTime)

        try {
            Thread.sleep(Math.max(25 - elapsedTime, 1))
        } catch (e: InterruptedException) {
            e.printStackTrace()
        }
    }

    private fun drawScene(image: BufferedImage) {
        val beginTime = System.currentTimeMillis()

        val g2 = prepareSceneAndGetGraphics(image)
        drawPlaceholder(g2)
        blockWidgets.forEach { it.draw(g2) }

        val endTime = System.currentTimeMillis()
        showInnerTime(g2, endTime - beginTime)
    }

    private fun prepareSceneAndGetGraphics(img: BufferedImage): Graphics2D {
        val g2 = img.createGraphics()
        g2.color = backgroundColor
        g2.fillRect(0, 0, w, h)
        g2.setRenderingHint(KEY_ANTIALIASING, VALUE_ANTIALIAS_ON)
        return g2
    }

    private fun drawPlaceholder(g2: Graphics2D) {
        g2.color = Color.yellow
        val NODE_RADIUS = 10
        g2.fillOval(
            500 - NODE_RADIUS,
            100 - NODE_RADIUS,
            NODE_RADIUS * 2,
            NODE_RADIUS * 2
        )
    }

    private fun processLogic() {
//        for (i in 0 until SKIP_FRAMES) scene.logic()
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
