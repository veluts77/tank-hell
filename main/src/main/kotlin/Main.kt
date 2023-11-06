import ui.Frame

fun main() {
    val f = Frame()
    f.title = "Tank Hell"
    Thread(f).start()
}