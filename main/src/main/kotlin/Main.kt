import ui.Form

fun main() {
    val f = Form()
    f.title = "Tank Hell"
    Thread(f).start()
}