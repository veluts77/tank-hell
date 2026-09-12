package ui

import domain.Tournament
import java.awt.BorderLayout
import java.awt.CardLayout
import javax.swing.JFrame
import javax.swing.JPanel

class Frame : JFrame(), Runnable {

    private val cards = CardLayout()
    private val root = JPanel(cards)
    private val playRoot = JPanel(BorderLayout())

    @Volatile
    private var playing = false
    private var controlPanel: ControlPanel? = null
    private var tournament: Tournament? = null

    init {
        defaultCloseOperation = EXIT_ON_CLOSE
        setLocation(50, 50)
        title = "Tank Hell"

        val select = PlayerSelectPanel { playerCount, roundCount ->
            startGame(playerCount, roundCount)
        }
        root.add(select, CARD_SELECT)
        root.add(playRoot, CARD_PLAY)
        add(root)

        pack()
        isVisible = true
    }

    private fun startGame(playerCount: Int, roundCount: Int) {
        tournament = Tournament(playerCount, roundCount).also { it.grantRoundBonuses() }
        showRound()
    }

    private fun startNextRound() {
        tournament?.let {
            it.startNextRound()
            it.grantRoundBonuses()
        }
        showRound()
    }

    private fun showRound() {
        val current = tournament ?: return
        playRoot.removeAll()

        val gamePanel = GamePanel(
            tournament = current,
            onNextRound = { startNextRound() },
            onBackToSelect = { backToSelect() }
        )
        val controls = ControlPanel(gamePanel.turnController)
        controlPanel = controls

        playRoot.add(gamePanel, BorderLayout.CENTER)
        playRoot.add(controls, BorderLayout.SOUTH)

        playing = true
        cards.show(root, CARD_PLAY)
        playRoot.revalidate()
        pack()
        gamePanel.requestFocusInWindow()
    }

    private fun backToSelect() {
        playing = false
        controlPanel = null
        tournament = null
        playRoot.removeAll()
        cards.show(root, CARD_SELECT)
        root.revalidate()
        pack()
    }

    override fun run() {
        while (true) {
            try {
                controlPanel?.refresh()
                repaint()
                if (!playing) {
                    Thread.sleep(25)
                }
            } catch (_: InterruptedException) {
                break
            }
        }
    }

    companion object {
        private const val CARD_SELECT = "select"
        private const val CARD_PLAY = "play"
    }
}
