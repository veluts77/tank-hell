package domain

data class WeaponSpec(
    val type: WeaponType,
    val isProjectile: Boolean,
    val explosionRadius: Int = 0,
    val centerDamage: Int = 0,
    val edgeDamage: Int = 0
) {
    companion object {
        const val CENTER_DAMAGE = 75
        const val EDGE_DAMAGE = 10

        val STANDARD = WeaponSpec(
            type = WeaponType.STANDARD,
            isProjectile = true,
            explosionRadius = 50,
            centerDamage = CENTER_DAMAGE,
            edgeDamage = EDGE_DAMAGE
        )
        val CLEANER = WeaponSpec(
            type = WeaponType.CLEANER,
            isProjectile = true,
            explosionRadius = 120,
            centerDamage = 0,
            edgeDamage = 0
        )
        val HEAVY = WeaponSpec(
            type = WeaponType.HEAVY,
            isProjectile = true,
            explosionRadius = 20,
            centerDamage = CENTER_DAMAGE * 3,
            edgeDamage = EDGE_DAMAGE * 2
        )
        val NUKE = WeaponSpec(WeaponType.NUKE, isProjectile = false)
        val SHIELD = WeaponSpec(WeaponType.SHIELD, isProjectile = false)
        val MEDKIT = WeaponSpec(WeaponType.MEDKIT, isProjectile = false)

        fun of(type: WeaponType) = when (type) {
            WeaponType.STANDARD -> STANDARD
            WeaponType.CLEANER -> CLEANER
            WeaponType.NUKE -> NUKE
            WeaponType.HEAVY -> HEAVY
            WeaponType.SHIELD -> SHIELD
            WeaponType.MEDKIT -> MEDKIT
        }
    }
}
