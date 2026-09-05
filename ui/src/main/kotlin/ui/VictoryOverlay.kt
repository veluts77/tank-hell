package ui

import java.awt.Dimension
import java.awt.Font
import java.awt.GridBagConstraints
import java.awt.GridBagLayout
import java.awt.Insets
import javax.swing.JButton
import javax.swing.JPanel

class VictoryOverlay(
    onNewGame: () -> Unit
) : JPanel(GridBagLayout()) {

    init {
        isOpaque = false
        isVisible = false
        isFocusable = false
        alignmentX = CENTER_ALIGNMENT
        alignmentY = CENTER_ALIGNMENT
        preferredSize = Dimension(800, 600)

        val newGame = JButton("New Game")
        newGame.font = Font("SansSerif", Font.BOLD, 16)
        newGame.addActionListener { onNewGame() }

        val constraints = GridBagConstraints()
        constraints.insets = Insets(80, 0, 0, 0)
        add(newGame, constraints)
    }

    override fun getMaximumSize(): Dimension = preferredSize

    override fun getMinimumSize(): Dimension = preferredSize

    fun showResult() {
        isVisible = true
        revalidate()
        repaint()
    }
}
