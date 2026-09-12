package ui

import domain.PlayerArsenal
import domain.WeaponType
import domain.Wind
import widgets.BulletWidget
import widgets.TankWidget

class TurnController(
    private val tankWidgets: List<TankWidget>,
    private val arsenalFor: (playerIndex: Int) -> PlayerArsenal,
    private val onProjectile: (BulletWidget) -> Unit,
    private val onInstant: (WeaponType, TankWidget) -> Unit
) {
    private var activeIndex = 0
    private var shotInProgress = false
    private var actorThisShot: TankWidget? = null
    private var wind = Wind.random()

    fun activeTankWidget(): TankWidget? = tankWidgets.getOrNull(activeIndex)

    fun activeIndex() = activeIndex

    fun shotInProgress() = shotInProgress

    fun canControl() = !shotInProgress && tankWidgets.size > 1

    fun wind() = wind

    fun activeArsenal(): PlayerArsenal? {
        val tankWidget = activeTankWidget() ?: return null
        return arsenalFor(tankWidget.playerIndex())
    }

    fun adjustAngle(delta: Int) {
        if (!canControl()) return
        activeTankWidget()?.adjustAimAngle(delta)
    }

    fun adjustPower(delta: Int) {
        if (!canControl()) return
        activeTankWidget()?.adjustPower(delta)
    }

    fun cycleWeapon() {
        if (!canControl()) return
        activeArsenal()?.cycle()
    }

    fun fire() {
        if (!canControl()) return
        val tankWidget = activeTankWidget() ?: return
        val arsenal = arsenalFor(tankWidget.playerIndex())
        val spec = arsenal.selected.spec()
        if (!arsenal.tryConsume()) return

        actorThisShot = tankWidget
        if (spec.isProjectile) {
            val (x, y) = tankWidget.muzzlePoint()
            onProjectile(
                BulletWidget(x, y, tankWidget.aimAngleDegrees(), tankWidget.power(), wind, tankWidget, spec)
            )
        } else {
            onInstant(spec.type, tankWidget)
        }
        shotInProgress = true
    }

    fun onWorldSettled() {
        if (!shotInProgress) return
        shotInProgress = false
        val actor = actorThisShot
        actor?.onTurnEnded()
        actorThisShot = null
        if (tankWidgets.isEmpty()) return
        val actorIndex = actor?.let { tankWidgets.indexOf(it) } ?: -1
        activeIndex = if (actorIndex >= 0) {
            (actorIndex + 1) % tankWidgets.size
        } else {
            activeIndex % tankWidgets.size
        }
        wind = Wind.random()
    }

    fun statusText(): String = if (shotInProgress) "WAIT..." else "READY"
}
