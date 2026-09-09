package ui

import domain.Tournament
import java.awt.Color
import java.awt.Dimension
import java.awt.Font
import java.awt.GridBagConstraints
import java.awt.GridBagLayout
import java.awt.GridLayout
import java.awt.Insets
import javax.swing.JButton
import javax.swing.JLabel
import javax.swing.JPanel
import javax.swing.SwingConstants

class VictoryOverlay(
    private val onNextRound: () -> Unit,
    private val onNewGame: () -> Unit
) : JPanel(GridBagLayout()) {

    private val titleLabel = JLabel("", SwingConstants.CENTER)
    private val detailLabel = JLabel("", SwingConstants.CENTER)
    private val tablePanel = JPanel()
    private val actionButton = JButton()
    private var tournamentFinished = false

    init {
        isOpaque = false
        isVisible = false
        isFocusable = false
        alignmentX = CENTER_ALIGNMENT
        alignmentY = CENTER_ALIGNMENT
        preferredSize = Dimension(800, 600)

        titleLabel.font = Font("SansSerif", Font.BOLD, 32)
        titleLabel.foreground = Color.WHITE
        titleLabel.alignmentX = CENTER_ALIGNMENT

        detailLabel.font = Font("SansSerif", Font.PLAIN, 18)
        detailLabel.foreground = Color(220, 220, 230)
        detailLabel.alignmentX = CENTER_ALIGNMENT

        tablePanel.isOpaque = false
        tablePanel.alignmentX = CENTER_ALIGNMENT

        actionButton.font = Font("SansSerif", Font.BOLD, 16)
        actionButton.addActionListener {
            actionButton.isEnabled = false
            if (tournamentFinished) onNewGame() else onNextRound()
        }

        val constraints = GridBagConstraints()
        constraints.gridx = 0
        constraints.fill = GridBagConstraints.HORIZONTAL
        constraints.insets = Insets(0, 0, 12, 0)
        add(titleLabel, constraints)

        constraints.insets = Insets(0, 0, 16, 0)
        add(detailLabel, constraints)

        constraints.fill = GridBagConstraints.NONE
        constraints.insets = Insets(0, 0, 24, 0)
        add(tablePanel, constraints)

        constraints.insets = Insets(0, 0, 0, 0)
        add(actionButton, constraints)
    }

    override fun getMaximumSize(): Dimension = preferredSize

    override fun getMinimumSize(): Dimension = preferredSize

    fun showResult(tournament: Tournament, roundHeadline: String) {
        tournamentFinished = tournament.isFinished()
        if (tournamentFinished) {
            titleLabel.text = "Tournament over"
            detailLabel.text = roundHeadline
            detailLabel.isVisible = true
            actionButton.text = "New Game"
        } else {
            titleLabel.text = roundHeadline
            detailLabel.text = "Round ${tournament.currentRound} of ${tournament.roundCount}"
            detailLabel.isVisible = true
            actionButton.text = "Next Round"
        }
        rebuildTable(tournament)
        actionButton.isEnabled = true
        isVisible = true
        revalidate()
        repaint()
    }

    private fun rebuildTable(tournament: Tournament) {
        tablePanel.removeAll()
        tablePanel.layout = GridLayout(tournament.playerCount, 2, 32, 6)
        tournament.standings().forEach { standing ->
            val color = TankPalette.COLORS[standing.playerIndex]
            val name = JLabel(TankPalette.displayName(color))
            name.font = Font("SansSerif", Font.BOLD, 16)
            name.foreground = if (color == Color.gray) Color.lightGray else color
            val score = JLabel(standing.score.toString(), SwingConstants.RIGHT)
            score.font = Font("SansSerif", Font.BOLD, 16)
            score.foreground = Color.WHITE
            tablePanel.add(name)
            tablePanel.add(score)
        }
        tablePanel.revalidate()
    }
}
