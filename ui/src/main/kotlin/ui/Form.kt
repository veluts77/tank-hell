package ui

import javax.swing.JFrame

class Form : JFrame(), Runnable {

    init {
        defaultCloseOperation = EXIT_ON_CLOSE
        setLocation(50, 50)
        val panel = Panel()
        add(panel)
        pack()
        isVisible = true
    }

    override fun run() {
        while (true) repaint()
    }
}
