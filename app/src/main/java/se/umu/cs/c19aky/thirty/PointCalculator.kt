package se.umu.cs.c19aky.thirty

import android.os.Bundle
import android.util.Log

private const val TAG = "PointCalculator"

private const val KEY_POINTS = "se.umu.cs.c19aky.categories"

class PointCalculator(numberOfCategories: Int = 9) {

    private var categories: MutableMap<String, Int> = mutableMapOf()

    init {
        categories["Low"] = -1
        for (x in 4 until numberOfCategories + 4) {
            categories[x.toString()] = -1
        }
    }

    // Store categories, for restoring when able to
    fun storeCategories(outState: Bundle) {
        Log.d(TAG, categories.values.toString())
        val toSave: ArrayList<Int> = arrayListOf()
        toSave.addAll(categories.values)
        outState.putIntegerArrayList(KEY_POINTS, toSave)
    }

    // Restore categories
    fun restoreCategories(outState: Bundle) {
        val savedCategories: ArrayList<Int> = outState.getIntegerArrayList(KEY_POINTS) as ArrayList<Int>
        categories["Low"] = savedCategories[0]
        for (x in 1 until savedCategories.size) {
            Log.d(TAG, (x+3).toString())
            categories[(x+3).toString()] = savedCategories[x]
        }
    }

    // Check if a given category is chosen
    fun checkIfCategoryIsChosen(category: String): Boolean {
        return categories[category] != -1
    }

    // Calculate points
    fun calculatePoints(targetSum: Int, values: ArrayList<Int>): Int {
        var sum = 0
        for (value in values) {
            sum += value
        }
        return if (sum % targetSum == 0) {
            sum
        } else {
            -1
        }
    }

    // Sum all values from 0 to 3
    fun calculatePointsLow(values: ArrayList<Int>): Int {
        var sum = 0
        for (value in values) {
            sum += if (value <= 3) {
                value
            } else {
                0
            }
        }
        return sum
    }

    // Add points to some category
    fun addPoints(points: Int, category: String) {
        categories[category] = points
    }

    // Get points in some category
    fun getPoints(category: String): Int? {
        return categories[category]
    }

    // Get the total amount of points from all categories
    fun getTotalPoints(): Int {
        var sum = 0
        for (x in categories.values) {
            sum += x
        }
        return sum
    }

    // Get a list of points, each entry is the amount of points in that category
    fun getAllPoints(): ArrayList<Int> {
        return ArrayList(categories.values)
    }

    // Get all categories
    fun getAllCategories(): ArrayList<String> {
        return ArrayList(categories.keys)
    }
}