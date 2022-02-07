package se.umu.cs.c19aky.thirty

import android.os.Bundle
import android.util.Log

private const val MAX_ROUNDS = 3

private const val STATE_COUNT_PHASE = "countPhase"
private const val STATE_ROUND_COUNTER = "currentRound"

private const val TAG = "GameLogic"

class GameLogic {

    private var pointCalculator: PointCalculator = PointCalculator()
    private var round: Int = 0
    private var countPhase: Boolean = false

    fun saveInstance(outState: Bundle) {
        Log.d(TAG, "Saving instance")
        outState.putInt(STATE_ROUND_COUNTER, round)
        outState.putBoolean(STATE_COUNT_PHASE, countPhase)
        pointCalculator.storeCategories(outState)
    }

    fun restoreInstance(savedInstanceState: Bundle) {
        Log.d(TAG, "Restoring instance")
        round = savedInstanceState.getInt(STATE_ROUND_COUNTER)
        countPhase = savedInstanceState.getBoolean(STATE_COUNT_PHASE)
        pointCalculator.restoreCategories(savedInstanceState)
    }

    fun getPoints(): ArrayList<Int> {
        return pointCalculator.getAllPoints()
    }

    fun getTotalPoints(): Int {
        return pointCalculator.getTotalPoints()
    }

    fun isGameDone(): Boolean {
        return round >= MAX_ROUNDS
    }

    fun resetRounds() {
        round = 0
    }

    // Start a new game
    fun newGame(diceViewModel: DiceViewModel) {
        round += 1
        countPhase = false

        diceViewModel.clearLockedDice()
        diceViewModel.clearUsedDice()
        diceViewModel.setThrowsLeft(3)

        pointCalculator.unselectCategory()
    }

    fun nextRound(diceViewModel: DiceViewModel) {
        diceViewModel.throwDice()
        if (diceViewModel.getThrowsLeft() == 0) {
            countPhase = true
        }
    }

    fun getCountPhase(): Boolean {
        return countPhase
    }

    // Run the count phase, calculating points in some category.
    fun runCountPhase(diceViewModel: DiceViewModel): Boolean {
        val sum = pointCalculator.calculatePoints(diceViewModel.getAllUnusedDiceValues(), diceViewModel.getLockedDiceValues())

        // Check if combination didn't work
        if (sum == -1) {
            return false
        }

        // If no dice were locked, but a sum was still found that means the category "Low" was used
        if (sum >= 0 && diceViewModel.getLockedDiceValues().size == 0) {
            diceViewModel.setAllDiceUsed()
        }

        // Add points
        pointCalculator.addPoints(sum)
        return true
    }

    fun categorySelected(category: String): Boolean {
        return pointCalculator.selectCategory(category)
    }
}