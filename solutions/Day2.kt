package solutions

import Utils.FileUtils
import Utils.StringUtils

class Day2: Solution {
    companion object {
        private val ballCountByColour: Map<String, Int> = mapOf(
                "red" to 12,
                "green" to 13,
                "blue" to 14,
        )

    }
    override fun solve(inputPath: String) {
        val input = FileUtils().readLineSequence(inputPath)
        var resultA = 0
        var resultB = 0
        for (line in input) {
            val gameData = line.split(':')
            val gameNumber = StringUtils().tryParseInt(gameData[0]) ?: 0
            val roundData = gameData[1].split(';')
            if (isGameValid(roundData)) {
                resultA += gameNumber
            }

            resultB += findPowerOfMinimalSetOfCubes(roundData)
        }

        println("partA: $resultA")
        println("partB: $resultB")
    }

    private fun isGameValid(roundData: List<String>): Boolean {
        for (round in roundData) {
            val drawData = round.split(',')
            if (!isRoundValid(drawData)) {
                return false
            }
        }
        return true
    }

    private fun isRoundValid(drawData: List<String>) : Boolean {
        val roundBallCountByColour: MutableMap<String, Int> = mutableMapOf()
        for (draw in drawData) {
            val ballCount = StringUtils().tryParseInt(draw) ?: 0 //this 0 shouldn't happen
            for ((colour, max) in ballCountByColour) {
                if (draw.contains(colour)) {
                    val currentCount = roundBallCountByColour.getOrDefault(colour, 0)
                    val newCount = currentCount + ballCount
                    roundBallCountByColour[colour] = newCount
                    if (newCount > max) {
                        return false
                    }

                }
            }
        }
        return true
    }

    private fun findPowerOfMinimalSetOfCubes(roundData: List<String>): Int {
        val maxBallCountsByColour: MutableMap<String, Int> = mutableMapOf()
        for (round in roundData) {
            val drawData = round.split(',')
            for (draw in drawData) {
                val ballCount = StringUtils().tryParseInt(draw) ?: 0 //this 0 shouldn't happen
                for (colour in ballCountByColour.keys) {
                    if (draw.contains(colour)) {
                        val currentMax = maxBallCountsByColour.getOrDefault(colour, 0)
                        maxBallCountsByColour[colour] = Math.max(currentMax, ballCount)
                    }
                }
            }
        }
        return maxBallCountsByColour.values.fold(1) { acc, i -> acc * i }
    }
}