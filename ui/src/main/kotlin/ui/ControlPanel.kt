package ui

import java.awt.FlowLayout
import javax.swing.JButton
import javax.swing.JLabel
import javax.swing.JPanel

class ControlPanel(
    private val turnController: TurnController
) : JPanel(FlowLayout(FlowLayout.LEFT, 8, 8)) {

    private val activeLabel = JLabel()
    private val angleLabel = JLabel()
    private val powerLabel = JLabel()
    private val statusLabel = JLabel()

    private val angleMinus = JButton("Angle −")
    private val anglePlus = JButton("Angle +")
    private val powerMinus = JButton("Power −")
    private val powerPlus = JButton("Power +")
    private val fireButton = JButton("Fire")

    init {
        angleMinus.addActionListener { turnController.adjustAngle(-1) }
        anglePlus.addActionListener { turnController.adjustAngle(1) }
        powerMinus.addActionListener { turnController.adjustPower(-1) }
        powerPlus.addActionListener { turnController.adjustPower(1) }
        fireButton.addActionListener { turnController.fire() }

        add(activeLabel)
        add(angleLabel)
        add(powerLabel)
        add(statusLabel)
        add(angleMinus)
        add(anglePlus)
        add(powerMinus)
        add(powerPlus)
        add(fireButton)

        refresh()
    }

    fun refresh() {
        val tankWidget = turnController.activeTankWidget()
        val canControl = turnController.canControl()

        activeLabel.text = tankWidget?.let { "Active: Tank #${turnController.activeIndex() + 1}" } ?: "Active: —"
        angleLabel.text = "Angle: ${tankWidget?.aimAngleDegrees() ?: 0}°"
        powerLabel.text = "Power: ${tankWidget?.power() ?: 0}"
        statusLabel.text = turnController.statusText()

        angleMinus.isEnabled = canControl
        anglePlus.isEnabled = canControl
        powerMinus.isEnabled = canControl
        powerPlus.isEnabled = canControl
        fireButton.isEnabled = canControl
    }
}
