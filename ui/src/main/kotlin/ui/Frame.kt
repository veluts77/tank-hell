package ui

import javax.swing.JFrame

class Frame : JFrame(), Runnable {

    init {
        defaultCloseOperation = EXIT_ON_CLOSE
        setLocation(50, 50)
        add(GamePanel())
        pack()
        isVisible = true
    }

    override fun run() {
        while (true) repaint()
    }
}
