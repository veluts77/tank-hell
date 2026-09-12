package domain

class PlayerArsenal(
    cleanerCount: Int,
    selfDestructCount: Int = 1
) {
    private val counts = mutableMapOf(
        WeaponType.CLEANER to cleanerCount,
        WeaponType.NUKE to selfDestructCount,
        WeaponType.HEAVY to 0,
        WeaponType.SHIELD to 0,
        WeaponType.MEDKIT to 0
    )

    var selected: WeaponType = WeaponType.STANDARD
        private set

    fun count(type: WeaponType): Int {
        if (type == WeaponType.STANDARD) return Int.MAX_VALUE
        return counts[type] ?: 0
    }

    fun hasAmmo(type: WeaponType) = type == WeaponType.STANDARD || count(type) > 0

    fun cycle() {
        val order = WeaponType.CYCLE_ORDER
        val currentIndex = order.indexOf(selected).coerceAtLeast(0)
        for (offset in 1..order.size) {
            val candidate = order[(currentIndex + offset) % order.size]
            if (hasAmmo(candidate)) {
                selected = candidate
                return
            }
        }
    }

    fun tryConsume(): Boolean {
        if (!hasAmmo(selected)) {
            selected = WeaponType.STANDARD
            return false
        }
        if (selected != WeaponType.STANDARD) {
            counts[selected] = count(selected) - 1
            if (count(selected) <= 0) {
                selected = WeaponType.STANDARD
            }
        }
        return true
    }

    fun add(type: WeaponType, amount: Int = 1) {
        if (type == WeaponType.STANDARD || amount == 0) return
        counts[type] = (counts[type] ?: 0) + amount
    }
}
