import ui.Form

fun main() {
    val f = Form()
    f.title = "Kotlin Objects"
    Thread(f).start()
}