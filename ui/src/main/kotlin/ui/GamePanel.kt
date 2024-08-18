package ui

import widgets.ExplosionWidget
import widgets.FallingDustBlockWidget
import widgets.GameFieldWidget
import widgets.TankWidget
import java.awt.Color
import java.awt.Dimension
import java.awt.Graphics
import java.awt.Graphics2D
import java.awt.event.MouseAdapter
import java.awt.event.MouseEvent
import java.awt.image.BufferedImage
import javax.swing.JPanel
import kotlin.math.max

class GamePanel: JPanel() {

    private var isHealthy = true

    private val w = 800
    private val h = 600

    private val imgBuffer = BufferedImage(
        w, h, BufferedImage.TYPE_INT_RGB)

    private val tankWidgets = mutableListOf<TankWidget>()
    private val blockWidgets = mutableListOf<FallingDustBlockWidget>()
    private val explosionWidgets = mutableListOf<ExplosionWidget>()
    private val gameFieldWidget = GameFieldWidget(w, h)


    init {
        preferredSize = Dimension(w, h)

        addTanks()

        addMouseListener(object : MouseAdapter() {
            override fun mousePressed(e: MouseEvent?) {
                if (e != null)
                    explosionWidgets.add(ExplosionWidget(e.x, e.y, 100, 10))
            }
        })
    }

    private fun addTanks() {
        tankWidgets.add(TankWidget(100, 50, Color.orange))
        tankWidgets.add(TankWidget(300, 50, Color.gray))
        tankWidgets.add(TankWidget(600, 50, Color.magenta))
    }

    override fun paintComponent(g: Graphics?) {
        try {
            val beginTime = System.currentTimeMillis()

            drawScene(imgBuffer)
            if (isHealthy) processLogic()
            (g as Graphics2D).drawImage(imgBuffer, 0, 0, null)

            val elapsedTime = System.currentTimeMillis() - beginTime
            showTime(g, elapsedTime)

            Thread.sleep(max(25 - elapsedTime, 1))
        } catch (e: InterruptedException) {
            e.printStackTrace()
        } catch (e: Exception) {
            isHealthy = false
            e.printStackTrace()
        }
    }

    private fun drawScene(image: BufferedImage) {
        val beginTime = System.currentTimeMillis()

        val g2 = image.createGraphics()
        gameFieldWidget.draw(g2)
        blockWidgets.forEach { it.draw(g2) }
        tankWidgets.forEach { it.draw(g2) }
        explosionWidgets.forEach { it.draw(g2) }

        val endTime = System.currentTimeMillis()
        showInnerTime(g2, endTime - beginTime)
    }

    private fun processLogic() {
        processFallingBlocks()
        processExplosions()
        processTanks()
    }

    private fun processFallingBlocks() {
        val toRemove = HashSet<FallingDustBlockWidget>()
        blockWidgets.forEach {
            it.tick()
            if (it.completed()) {
                it.applyTo(gameFieldWidget)
                toRemove.add(it)
            }
        }
        blockWidgets.removeAll(toRemove)
    }

    private fun processExplosions() {
        val toRemove = HashSet<ExplosionWidget>()
        explosionWidgets.forEach {
            it.tick()
            if (it.completed()) {
                toRemove.add(it)
                blockWidgets.add(it.createFallingBlockWidgetOn(gameFieldWidget.gameField()))
            }
        }
        explosionWidgets.removeAll(toRemove)
    }

    private fun processTanks() {
        val toRemove = HashSet<TankWidget>()
        tankWidgets.forEach {
            it.tick()
            processTankFalling(it)
            val destroyed = processTankWounds(it)
            if (destroyed) {
                toRemove.add(it)
                explosionWidgets.add(it.explode())
            }
        }
        tankWidgets.removeAll(toRemove)
    }

    private fun processTankWounds(tankWidget: TankWidget): Boolean {
        return false
    }

    private fun processTankFalling(tankWidget: TankWidget) {
        val gameField = gameFieldWidget.gameField()
        val area = tankWidget.area()
        val tankBottomY = area.y + area.height
        val tankMiddleX = area.x + area.width / 2
        if (tankBottomY >= h || gameField.at(tankMiddleX, tankBottomY)) tankWidget.stopFalling()
        else {
            tankWidget.startFalling()
            gameField.applySubMatrix(area.x, area.y, emptyMatrix(area.width, area.height))
        }
    }

    private fun emptyMatrix(width: Int, height: Int) = Array(width) {
        BooleanArray(height) {
            false
        }
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