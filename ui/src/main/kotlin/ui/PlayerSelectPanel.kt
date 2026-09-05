package ui

import java.awt.Color
import java.awt.Dimension
import java.awt.Font
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
    private val onStart: (Int) -> Unit
) : JPanel(GridBagLayout()) {

    private var selectedCount = DEFAULT_COUNT

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

        val subtitle = JLabel("Select players", SwingConstants.CENTER)
        subtitle.font = Font("SansSerif", Font.PLAIN, 20)
        subtitle.foreground = Color(200, 200, 210)
        subtitle.alignmentX = CENTER_ALIGNMENT

        val countRow = JPanel()
        countRow.isOpaque = false
        countRow.alignmentX = CENTER_ALIGNMENT
        val group = ButtonGroup()
        for (count in MIN_COUNT..MAX_COUNT) {
            val button = JToggleButton(count.toString())
            button.font = Font("SansSerif", Font.BOLD, 18)
            button.preferredSize = Dimension(56, 40)
            button.isSelected = count == DEFAULT_COUNT
            button.addActionListener { selectedCount = count }
            group.add(button)
            countRow.add(button)
        }

        val start = JButton("Start")
        start.font = Font("SansSerif", Font.BOLD, 18)
        start.alignmentX = CENTER_ALIGNMENT
        start.addActionListener { onStart(selectedCount) }

        content.add(title)
        content.add(Box.createVerticalStrut(16))
        content.add(subtitle)
        content.add(Box.createVerticalStrut(28))
        content.add(countRow)
        content.add(Box.createVerticalStrut(28))
        content.add(start)

        add(content)
    }

    companion object {
        const val MIN_COUNT = 2
        const val MAX_COUNT = 6
        const val DEFAULT_COUNT = 3
    }
}
