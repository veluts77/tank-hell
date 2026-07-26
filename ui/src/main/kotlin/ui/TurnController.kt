package ui

import widgets.BulletWidget
import widgets.TankWidget

class TurnController(
    private val tankWidgets: List<TankWidget>,
    private val onFire: (BulletWidget) -> Unit
) {
    private var activeIndex = 0
    private var shotInProgress = false

    fun activeTankWidget(): TankWidget? = tankWidgets.getOrNull(activeIndex)

    fun activeIndex() = activeIndex

    fun shotInProgress() = shotInProgress

    fun canControl() = !shotInProgress && tankWidgets.isNotEmpty()

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
        val tank = activeTankWidget() ?: return
        val (x, y) = tank.muzzlePoint()
        onFire(BulletWidget(x, y, tank.aimAngleDegrees(), tank.power()))
        shotInProgress = true
    }

    fun onWorldSettled() {
        if (!shotInProgress) return
        shotInProgress = false
        if (tankWidgets.isEmpty()) return
        activeIndex = (activeIndex + 1) % tankWidgets.size
    }

    fun statusText(): String = if (shotInProgress) "WAIT..." else "READY"
}
