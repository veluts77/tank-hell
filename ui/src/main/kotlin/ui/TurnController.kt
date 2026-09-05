package ui

import domain.Wind
import widgets.BulletWidget
import widgets.TankWidget

class TurnController(
    private val tankWidgets: List<TankWidget>,
    private val onFire: (BulletWidget) -> Unit
) {
    private var activeIndex = 0
    private var shotInProgress = false
    private var wind = Wind.random()

    fun activeTankWidget(): TankWidget? = tankWidgets.getOrNull(activeIndex)

    fun activeIndex() = activeIndex

    fun shotInProgress() = shotInProgress

    fun canControl() = !shotInProgress && tankWidgets.size > 1

    fun wind() = wind

    fun adjustAngle(delta: Int) {
        if (!canControl()) return
        activeTankWidget()?.adjustAimAngle(delta)
    }

    fun adjustPower(delta: Int) {
        if (!canControl()) return
        activeTankWidget()?.adjustPower(delta)
    }

    fun fire() {
        if (!canControl()) return
        val tankWidget = activeTankWidget() ?: return
        val (x, y) = tankWidget.muzzlePoint()
        onFire(BulletWidget(x, y, tankWidget.aimAngleDegrees(), tankWidget.power(), wind, tankWidget))
        shotInProgress = true
    }

    fun onWorldSettled() {
        if (!shotInProgress) return
        shotInProgress = false
        if (tankWidgets.isEmpty()) return
        activeIndex = (activeIndex + 1) % tankWidgets.size
        wind = Wind.random()
    }

    fun statusText(): String = if (shotInProgress) "WAIT..." else "READY"
}
