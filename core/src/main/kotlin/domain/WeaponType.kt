package domain

enum class WeaponType(
    val displayName: String,
    val shortName: String
) {
    STANDARD("Standard", "Std"),
    CLEANER("Cleaner", "Cln"),
    NUKE("Nuke", "Nuk"),
    HEAVY("Heavy", "Hv"),
    SHIELD("Shield", "Sh"),
    MEDKIT("Medkit", "Med");

    fun spec() = WeaponSpec.of(this)

    companion object {
        val CYCLE_ORDER = listOf(
            STANDARD,
            CLEANER,
            NUKE,
            HEAVY,
            SHIELD,
            MEDKIT
        )

        val BONUS_TYPES = listOf(HEAVY, SHIELD, MEDKIT)
    }
}
