package domain

class Tournament(
    val playerCount: Int,
    val roundCount: Int
) {
    private val scores = IntArray(playerCount)
    private val eliminationOrder = mutableListOf<Int>()

    var currentRound = 1
        private set

    var roundFinished = false
        private set

    fun score(playerIndex: Int) = scores[playerIndex]

    fun creditKill(killerIndex: Int, victimIndex: Int) {
        if (killerIndex == victimIndex) return
        scores[killerIndex] += KILL
    }

    fun recordElimination(playerIndex: Int) {
        eliminationOrder.add(playerIndex)
    }

    fun finishRound(survivorIndex: Int?) {
        if (roundFinished) return
        survivorIndex?.let { scores[it] += WIN }
        val fromLast = eliminationOrder.asReversed()
        if (playerCount >= 3) {
            fromLast.getOrNull(0)?.let { scores[it] += SECOND }
        }
        if (playerCount >= 4) {
            fromLast.getOrNull(1)?.let { scores[it] += THIRD }
        }
        roundFinished = true
    }

    fun startNextRound() {
        currentRound++
        eliminationOrder.clear()
        roundFinished = false
    }

    fun isFinished() = roundFinished && currentRound >= roundCount

    fun standings(): List<Standing> {
        return (0 until playerCount)
            .map { Standing(it, scores[it]) }
            .sortedByDescending { it.score }
    }

    data class Standing(val playerIndex: Int, val score: Int)

    companion object {
        const val KILL = 1
        const val WIN = 5
        const val SECOND = 3
        const val THIRD = 1
        const val MIN_ROUNDS = 1
        const val MAX_ROUNDS = 10
        const val DEFAULT_ROUNDS = 3
    }
}
