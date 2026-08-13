package ui

import widgets.*
import java.awt.Color
import java.awt.Dimension
import java.awt.Graphics
import java.awt.Graphics2D
import java.awt.Polygon
import java.awt.event.ActionEvent
import java.awt.event.KeyEvent
import java.awt.image.BufferedImage
import javax.swing.AbstractAction
import javax.swing.JComponent
import javax.swing.JPanel
import javax.swing.KeyStroke
import kotlin.math.max

class GamePanel : JPanel() {

    private var isHealthy = true

    private val w = 800
    private val h = 600

    private val imgBuffer = BufferedImage(
        w, h, BufferedImage.TYPE_INT_RGB)

    private val tankWidgets = mutableListOf<TankWidget>()
    private val blockWidgets = mutableListOf<FallingDustBlockWidget>()
    private val explosionWidgets = mutableListOf<ExplosionWidget>()
    private val bulletWidgets = mutableListOf<BulletWidget>()
    private val gameFieldWidget = GameFieldWidget(w, h)

    val turnController = TurnController(tankWidgets) { bullet ->
        bulletWidgets.add(bullet)
    }

    init {
        preferredSize = Dimension(w, h)
        isFocusable = true

        addTanks()
        installKeyBindings()
    }

    private fun addTanks() {
        tankWidgets.add(TankWidget(100, 50, Color.orange))
        tankWidgets.add(TankWidget(300, 50, Color.gray))
        tankWidgets.add(TankWidget(600, 50, Color.magenta))
    }

    private fun installKeyBindings() {
        val inputMap = getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW)
        val actionMap = actionMap

        fun bind(name: String, keyStroke: KeyStroke, action: () -> Unit) {
            inputMap.put(keyStroke, name)
            actionMap.put(name, object : AbstractAction() {
                override fun actionPerformed(e: ActionEvent?) {
                    action()
                }
            })
        }

        bind("angleLeft", KeyStroke.getKeyStroke(KeyEvent.VK_LEFT, 0)) {
            turnController.adjustAngle(-1)
        }
        bind("angleRight", KeyStroke.getKeyStroke(KeyEvent.VK_RIGHT, 0)) {
            turnController.adjustAngle(1)
        }
        bind("angleLeftFast", KeyStroke.getKeyStroke(KeyEvent.VK_LEFT, KeyEvent.SHIFT_DOWN_MASK)) {
            turnController.adjustAngle(-5)
        }
        bind("angleRightFast", KeyStroke.getKeyStroke(KeyEvent.VK_RIGHT, KeyEvent.SHIFT_DOWN_MASK)) {
            turnController.adjustAngle(5)
        }
        bind("powerUp", KeyStroke.getKeyStroke(KeyEvent.VK_UP, 0)) {
            turnController.adjustPower(1)
        }
        bind("powerDown", KeyStroke.getKeyStroke(KeyEvent.VK_DOWN, 0)) {
            turnController.adjustPower(-1)
        }
        bind("fire", KeyStroke.getKeyStroke(KeyEvent.VK_SPACE, 0)) {
            turnController.fire()
        }
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
        drawActiveTankMarker(g2)
        explosionWidgets.forEach { it.draw(g2) }
        bulletWidgets.forEach { it.draw(g2) }
        drawHud(g2)

        val endTime = System.currentTimeMillis()
        showInnerTime(g2, endTime - beginTime)
        g2.dispose()
    }

    private fun drawActiveTankMarker(g2: Graphics2D) {
        val tankWidget = turnController.activeTankWidget() ?: return
        val a = tankWidget.area()
        val tipX = a.x + a.width / 2
        val tipY = a.y - 15
        g2.color = Color.yellow
        g2.fillPolygon(
            Polygon(
                intArrayOf(tipX, tipX - 6, tipX + 6),
                intArrayOf(tipY, tipY - 10, tipY - 10),
                3
            )
        )
    }

    private fun drawHud(g2: Graphics2D) {
        val tankWidget = turnController.activeTankWidget()
        val angle = tankWidget?.aimAngleDegrees() ?: 0
        val power = tankWidget?.power() ?: 0
        val tankLabel = tankWidget?.let { "Tank #${turnController.activeIndex() + 1}" } ?: "—"

        g2.color = Color.white
        g2.drawString("Active: $tankLabel", 10, 20)
        g2.drawString("Angle: $angle°", 10, 80)
        g2.drawString("Power: $power", 10, 96)
        g2.drawString(turnController.statusText(), 10, 112)
    }

    private fun processLogic() {
        processFallingBlocks()
        processExplosions()
        processTanks()
        processBullets()
        checkWorldSettled()
    }

    private fun checkWorldSettled() {
        if (!turnController.shotInProgress()) return
        if (bulletWidgets.isEmpty() && explosionWidgets.isEmpty() && blockWidgets.isEmpty()) {
            turnController.onWorldSettled()
        }
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
            if (it.isDestroyed()) {
                toRemove.add(it)
                val explosion = it.explode()
                explosionWidgets.add(explosion)
                applyExplosionDamage(explosion)
            }
        }
        tankWidgets.removeAll(toRemove)
    }

    private fun applyExplosionDamage(explosionWidget: ExplosionWidget) {
        tankWidgets.forEach { tankWidget ->
            tankWidget.applyDamage(explosionWidget.damageFor(tankWidget.area()))
        }
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

    private fun processBullets() {
        val toRemove = HashSet<BulletWidget>()
        val gameField = gameFieldWidget.gameField()
        bulletWidgets.forEach {
            it.tick()
            if (it.flownAwayFrom(gameField)) {
                toRemove.add(it)
            } else if (it.collided(gameField) || it.collided(tankWidgets)) {
                toRemove.add(it)
                val explosion = it.explode()
                explosionWidgets.add(explosion)
                applyExplosionDamage(explosion)
            }
        }
        bulletWidgets.removeAll(toRemove)
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
