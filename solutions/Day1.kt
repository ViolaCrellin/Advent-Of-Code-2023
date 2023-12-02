package solutions

import Utils.FileUtils

class Day1: Solution {

    companion object {
        private val digitWordsToNumbers = mapOf(
                "zero" to "0",
                "one" to "1",
                "two" to "2",
                "three" to "3",
                "four" to "4",
                "five" to "5",
                "six" to "6",
                "seven" to "7",
                "eight" to "8",
                "nine" to "9"
        )
        private val digitWordRegex = digitWordsToNumbers.keys.joinToString("|", "(", ")").toRegex()
    }

    override fun solve(path: String){
        val input = FileUtils().readLineSequence(path)
        var resultA = 0
        var resultB = 0
        var resultC = 0
        var resultD = 0
        for (line in input) {
            val firstDigit = line.find { it.isDigit() }
            val lastDigit = line.findLast { it.isDigit() }
            val togetherA = "$firstDigit$lastDigit".toIntOrNull() ?: 0
            resultA += togetherA

            val firstWordDigit = findWordDigit(line, true)
            val lastWordDigit = findWordDigit(line, false)
            val togetherB = "$firstWordDigit$lastWordDigit".toIntOrNull() ?: 0
            resultB += togetherB


            val firstDigitOrWordDigit = findDigitOrWordDigit(line, true)
            val lastDigitOrWordDigit = findDigitOrWordDigit(line, false)
            val togetherC = "$firstDigitOrWordDigit$lastDigitOrWordDigit".toIntOrNull() ?: 0
            resultC += togetherC

            println(line)
            val firstDigitOrWordDigitIter = findDigitOrWordDigitIteratively(line, true)
            val lastDigitOrWordDigitIter = findDigitOrWordDigitIteratively(line, false)
            val togetherD = "$firstDigitOrWordDigitIter$lastDigitOrWordDigitIter".toIntOrNull() ?: 0
            println(togetherD)
            resultD += togetherD
        }
        println("Solution A: $resultA")
        println("Solution B: $resultB")
        println("Solution C: $resultC") // 55445
        println("Solution D: $resultD") // 55413
    }

    private fun findWordDigit(line: String, findFirst: Boolean): String {
        val matchResult = if (findFirst) digitWordRegex.find(line) else digitWordRegex.findAll(line).lastOrNull()
        return matchResult?.let { digitWordsToNumbers[it.value] } ?: ""
    }

    private fun findDigitOrWordDigit(line: String, findFirst: Boolean): String {
        val digitMatchIndex = if (findFirst) line.indexOfFirst { it.isDigit() } else line.indexOfLast { it.isDigit() }
        var digitMatch = if (digitMatchIndex != -1) line[digitMatchIndex].toString() else ""

        val wordDigitMatchResult = if (findFirst) {
            digitWordRegex.find(line)
        } else {
            digitWordRegex.findAll(line).lastOrNull()
        }
        val wordDigitMatchIndex = wordDigitMatchResult?.range?.let { if (findFirst) it.first else it.last } ?: -1
        val wordDigitMatch = wordDigitMatchResult?.value?.let { digitWordsToNumbers[it] } ?: ""

        return when {
            digitMatch.isEmpty() && wordDigitMatch.isEmpty() -> ""
            digitMatch.isEmpty() -> wordDigitMatch
            wordDigitMatch.isEmpty() -> digitMatch
            findFirst -> if (digitMatchIndex < wordDigitMatchIndex) digitMatch else wordDigitMatch
            else -> if (digitMatchIndex > wordDigitMatchIndex) digitMatch else wordDigitMatch
        }
    }

    private fun findDigitOrWordDigitIteratively(line: String, findFirst: Boolean): String {
        var matchIndex = -1
        var matchString = ""

        if (findFirst) {
            for (i in line.indices) {
                if (line[i].isDigit()) {
                    return line[i].toString()
                }
                searchDigitWords(line, i)?.let {
                    return it.second
                }
            }
        } else {
            for (i in line.indices.reversed()) {
                if (line[i].isDigit()) {
                    return line[i].toString()
                }
                searchDigitWords(line, i)?.let { match ->
                    // Check if the match extends beyond the current index i
                    if (match.first + match.second.length > i) {
                        return match.second
                    }
                }
            }
        }

        return ""
    }

    fun searchDigitWords(line: String, startIndex: Int): Pair<Int, String>? {
        digitWordsToNumbers.forEach { (word, number) ->
            if (line.regionMatches(startIndex, word, 0, word.length, ignoreCase = true)) {
                return startIndex to number
            }
        }
        return null
    }

}
