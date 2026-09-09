package ui

import domain.Tournament
import widgets.TankWidget
import java.awt.Color
import java.awt.Dimension
import java.awt.Font
import java.awt.Graphics
import java.awt.Graphics2D
import java.awt.GridBagLayout
import javax.swing.Box
import javax.swing.BoxLayout
import javax.swing.ButtonGroup
import javax.swing.JButton
import javax.swing.JLabel
import javax.swing.JPanel
import javax.swing.JToggleButton
import javax.swing.SwingConstants

class PlayerSelectPanel(
    private val onStart: (playerCount: Int, roundCount: Int) -> Unit
) : JPanel(GridBagLayout()) {

    private var selectedCount = DEFAULT_COUNT
    private var selectedRounds = Tournament.DEFAULT_ROUNDS

    init {
        preferredSize = Dimension(800, 600)
        background = Color(30, 32, 48)

        val content = JPanel()
        content.isOpaque = false
        content.layout = BoxLayout(content, BoxLayout.Y_AXIS)

        val title = JLabel("Tank Hell", SwingConstants.CENTER)
        title.font = Font("SansSerif", Font.BOLD, 42)
        title.foreground = Color.WHITE
        title.alignmentX = CENTER_ALIGNMENT

        val subtitle = sectionLabel("Select players")
        val preview = TankPreviewRow(DEFAULT_COUNT)
        val countRow = toggleRow(MIN_COUNT, MAX_COUNT, DEFAULT_COUNT) { count ->
            selectedCount = count
            preview.setCount(count)
        }

        val roundsSubtitle = sectionLabel("Select rounds")
        val roundsRow = toggleRow(
            Tournament.MIN_ROUNDS,
            Tournament.MAX_ROUNDS,
            Tournament.DEFAULT_ROUNDS
        ) { rounds ->
            selectedRounds = rounds
        }

        val start = JButton("Start")
        start.font = Font("SansSerif", Font.BOLD, 18)
        start.alignmentX = CENTER_ALIGNMENT
        start.addActionListener { onStart(selectedCount, selectedRounds) }

        content.add(title)
        content.add(Box.createVerticalStrut(16))
        content.add(subtitle)
        content.add(Box.createVerticalStrut(20))
        content.add(preview)
        content.add(Box.createVerticalStrut(20))
        content.add(countRow)
        content.add(Box.createVerticalStrut(20))
        content.add(roundsSubtitle)
        content.add(Box.createVerticalStrut(12))
        content.add(roundsRow)
        content.add(Box.createVerticalStrut(28))
        content.add(start)

        add(content)
    }

    private fun sectionLabel(text: String): JLabel {
        val label = JLabel(text, SwingConstants.CENTER)
        label.font = Font("SansSerif", Font.PLAIN, 20)
        label.foreground = Color(200, 200, 210)
        label.alignmentX = CENTER_ALIGNMENT
        return label
    }

    private fun toggleRow(
        min: Int,
        max: Int,
        defaultValue: Int,
        onSelect: (Int) -> Unit
    ): JPanel {
        val row = JPanel()
        row.isOpaque = false
        row.alignmentX = CENTER_ALIGNMENT
        val group = ButtonGroup()
        for (value in min..max) {
            val button = JToggleButton(value.toString())
            button.font = Font("SansSerif", Font.BOLD, 18)
            button.preferredSize = Dimension(48, 36)
            button.isSelected = value == defaultValue
            button.addActionListener { onSelect(value) }
            group.add(button)
            row.add(button)
        }
        return row
    }

    companion object {
        const val MIN_COUNT = 2
        const val MAX_COUNT = 6
        const val DEFAULT_COUNT = 3
    }
}

private class TankPreviewRow(
    private var count: Int
) : JPanel() {

    init {
        isOpaque = false
        alignmentX = CENTER_ALIGNMENT
        val width = MAX_COUNT * TankWidget.BODY_WIDTH + (MAX_COUNT - 1) * GAP
        preferredSize = Dimension(width, PREVIEW_HEIGHT)
        maximumSize = Dimension(Int.MAX_VALUE, PREVIEW_HEIGHT)
        minimumSize = Dimension(width, PREVIEW_HEIGHT)
    }

    fun setCount(count: Int) {
        this.count = count
        repaint()
    }

    override fun paintComponent(g: Graphics) {
        super.paintComponent(g)
        val g2 = g as Graphics2D
        val tankW = TankWidget.BODY_WIDTH
        val total = count * tankW + (count - 1) * GAP
        val startX = (width - total) / 2
        val y = (height - TankWidget.BODY_HEIGHT) / 2
        for (i in 0..<count) {
            TankWidget.paint(g2, startX + i * (tankW + GAP), y, TankPalette.COLORS[i])
        }
    }

    companion object {
        private const val GAP = 16
        private const val PREVIEW_HEIGHT = 40
        private const val MAX_COUNT = PlayerSelectPanel.MAX_COUNT
    }
}
