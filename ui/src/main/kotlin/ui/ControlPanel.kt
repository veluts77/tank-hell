package ui

import domain.PlayerArsenal
import domain.WeaponType
import java.awt.BorderLayout
import java.awt.FlowLayout
import javax.swing.JButton
import javax.swing.JLabel
import javax.swing.JPanel

class ControlPanel(
    private val turnController: TurnController
) : JPanel(BorderLayout(1, 1)) {

    private val labels = JPanel(FlowLayout(FlowLayout.LEFT, 8, 8))
    private val buttons = JPanel(FlowLayout(FlowLayout.LEFT, 8, 8))

    private val activeLabel = JLabel()
    private val angleLabel = JLabel()
    private val powerLabel = JLabel()
    private val windLabel = JLabel()
    private val statusLabel = JLabel()
    private val weaponLabel = JLabel()
    private val ammoLabel = JLabel()

    private val angleLeft = JButton("<<<")
    private val angleRight = JButton(">>>")
    private val powerMinus = JButton("Power −")
    private val powerPlus = JButton("Power +")
    private val ammoButton = JButton("Ammo")
    private val fireButton = JButton("Fire")

    init {
        angleLeft.addActionListener { turnController.adjustAngle(1) }
        angleRight.addActionListener { turnController.adjustAngle(-1) }
        powerMinus.addActionListener { turnController.adjustPower(-1) }
        powerPlus.addActionListener { turnController.adjustPower(1) }
        ammoButton.addActionListener { turnController.cycleWeapon() }
        fireButton.addActionListener { turnController.fire() }

        add(labels, BorderLayout.NORTH)
        add(buttons, BorderLayout.SOUTH)
        labels.add(activeLabel)
        labels.add(angleLabel)
        labels.add(powerLabel)
        labels.add(windLabel)
        labels.add(statusLabel)
        labels.add(weaponLabel)
        labels.add(ammoLabel)
        buttons.add(angleLeft)
        buttons.add(angleRight)
        buttons.add(powerMinus)
        buttons.add(powerPlus)
        buttons.add(ammoButton)
        buttons.add(fireButton)

        refresh()
    }

    fun refresh() {
        val tankWidget = turnController.activeTankWidget()
        val arsenal = turnController.activeArsenal()
        val canControl = turnController.canControl()

        activeLabel.text = tankWidget?.let { "Active: Tank #${turnController.activeIndex() + 1}" } ?: "Active: —"
        angleLabel.text = "Angle: ${tankWidget?.aimAngleDegrees() ?: 0}°"
        powerLabel.text = "Power: ${tankWidget?.power() ?: 0}"
        windLabel.text = turnController.wind().displayText()
        statusLabel.text = turnController.statusText()
        weaponLabel.text = "Weapon: ${arsenal?.selected?.displayName ?: "—"}"
        ammoLabel.text = arsenal?.let { formatAmmo(it) } ?: "Ammo: —"

        angleLeft.isEnabled = canControl
        angleRight.isEnabled = canControl
        powerMinus.isEnabled = canControl
        powerPlus.isEnabled = canControl
        ammoButton.isEnabled = canControl
        fireButton.isEnabled = canControl
    }

    private fun formatAmmo(arsenal: PlayerArsenal): String {
        return WeaponType.CYCLE_ORDER.joinToString("  ") { type ->
            val amount = if (type == WeaponType.STANDARD) "∞" else arsenal.count(type).toString()
            val token = "${type.shortName} $amount"
            if (type == arsenal.selected) "[$token]" else token
        }
    }
}
