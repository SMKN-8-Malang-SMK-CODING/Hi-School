package com.hischool.hischool.utils

import java.util.*

object StringSimilarity {
    /**
     * Calculates the similarity (a number within 0 and 1) between two strings.
     */
    fun similarity(s1: String, s2: String): Double {
        var longer = s1
        var shorter = s2

        if (s1.length < s2.length) {
            longer = s2
            shorter = s1
        }

        val longerLength = longer.length

        return if (longerLength == 0) {
            1.0
        } else (longerLength - editDistance(longer, shorter)) / longerLength.toDouble()
    }


    private fun editDistance(ps1: String, ps2: String): Int {
        var s1 = ps1
        var s2 = ps2

        s1 = s1.toLowerCase(Locale.getDefault())
        s2 = s2.toLowerCase(Locale.getDefault())

        val costs = IntArray(s2.length + 1)
        for (i in 0..s1.length) {
            var lastValue = i
            for (j in 0..s2.length) {
                if (i == 0)
                    costs[j] = j
                else {
                    if (j > 0) {
                        var newValue = costs[j - 1]
                        if (s1[i - 1] != s2[j - 1])
                            newValue = Math.min(
                                Math.min(newValue, lastValue),
                                costs[j]
                            ) + 1
                        costs[j - 1] = lastValue
                        lastValue = newValue
                    }
                }
            }
            if (i > 0)
                costs[s2.length] = lastValue
        }
        return costs[s2.length]
    }
}