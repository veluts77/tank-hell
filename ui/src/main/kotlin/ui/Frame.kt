package ui

import java.awt.BorderLayout
import javax.swing.JFrame
import javax.swing.JPanel

class Frame : JFrame(), Runnable {

    private val gamePanel = GamePanel()
    private val controlPanel = ControlPanel(gamePanel.turnController)

    init {
        defaultCloseOperation = EXIT_ON_CLOSE
        setLocation(50, 50)
        title = "Tank Hell"

        val root = JPanel(BorderLayout())
        root.add(gamePanel, BorderLayout.CENTER)
        root.add(controlPanel, BorderLayout.SOUTH)
        add(root)

        pack()
        isVisible = true
        gamePanel.requestFocusInWindow()
    }

    override fun run() {
        while (true) {
            controlPanel.refresh()
            repaint()
        }
    }
}
