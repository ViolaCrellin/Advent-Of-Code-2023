package solutions

import Utils.FileUtils
import Utils.StringUtils

class Day3: Solution {
    companion object {
        var engineSchematic:List<CharArray> = listOf()
        var maxCol: Int = 0
        var maxRow: Int = 0
        var allIndexesOfStars = mutableMapOf<Int,MutableList<Int>>()
    }
    override fun solve(inputPath: String) {
        val input = FileUtils().readLineSequence(inputPath)
        engineSchematic = StringUtils().buildMatrix(input.toList())
        maxRow = engineSchematic.size - 1
        maxCol = engineSchematic[0].size -1
        val allNumbers = mutableListOf<Int>()
        val starNumbers = mutableMapOf<Pair<Int, Int>, MutableList<Int>>()
        val allNumberEndCoords = mutableMapOf<Pair<Int, Int>, String>()
        var colIndex = 0
        for (rowIndex in engineSchematic.indices) {
            val foundNumbersNearSymbols = mutableListOf<Char>()
            var includeNumber = false
            while (colIndex < maxCol) {
                if (engineSchematic[rowIndex][colIndex] == '*') {
                    val starColIndexesInRow = allIndexesOfStars.getOrDefault(rowIndex, mutableListOf())
                    starColIndexesInRow.add(colIndex)
                    allIndexesOfStars[rowIndex] = starColIndexesInRow
                }
                while(engineSchematic[rowIndex][colIndex].isDigit()) {
                    foundNumbersNearSymbols.add(engineSchematic[rowIndex][colIndex])
                    // try to look up, down, left right for a non-digit, non-. symbol
                    if (hasSymbolAdjacent(rowIndex, colIndex)) {
                        includeNumber = true
                    }
                    colIndex++
                    if (colIndex > maxCol) {
                        break
                    }
                    if (engineSchematic[rowIndex][colIndex] == '*') {
                        val starColIndexesInRow = allIndexesOfStars.getOrDefault(rowIndex, mutableListOf())
                        starColIndexesInRow.add(colIndex)
                        allIndexesOfStars[rowIndex] = starColIndexesInRow
                    }
                }

                if (includeNumber) {
                    val strNumber = foundNumbersNearSymbols.joinToString("")
                    val number = StringUtils().tryParseInt(strNumber) ?: 0
                    allNumbers.add(number)
                    allNumberEndCoords[Pair(rowIndex, colIndex)] = strNumber
                    includeNumber = false
                }
                foundNumbersNearSymbols.clear()

                colIndex++
            }
            colIndex = 0
        }

        for ((coords, strNumber) in allNumberEndCoords) {
            val nearestStar = isNearStar(strNumber, coords.first, coords.second)
            if (nearestStar != null) {
                val gearComponents = starNumbers.getOrDefault(nearestStar, mutableListOf())
                val number = StringUtils().tryParseInt(strNumber) ?: 0
                gearComponents.add(number)
                starNumbers[nearestStar] = gearComponents
            }
        }

        var resultB = 0
        for (starNumberList in starNumbers.values) {
            if (starNumberList.size == 2) {
                resultB += starNumberList.fold(1) { acc, i -> acc * i }
            } else {
                println(starNumberList)
            }
        }

        val resultA = allNumbers.sumOf { it }

        println("ResultA $resultA")
        println("ResultB $resultB")
    }


    private fun isNearStar(strNumber: String, rowIndex: Int, colIndex: Int): Pair<Int, Int>? {
        //the rowIndex and colIndex here are the last in the  strNumber position-wise.
        val aboveRowIndex = rowIndex - 1
        val belowRowIndex = rowIndex + 1
        val rowIndexesToCheck = listOf(aboveRowIndex, rowIndex, belowRowIndex)
        for (rowIndexToCheck in rowIndexesToCheck) {
            if (rowIndexToCheck in allIndexesOfStars.keys) {
                for (i in 0 until strNumber.length + 2) {
                    val colIndexToCheck = colIndex - i
                    if (allIndexesOfStars[rowIndexToCheck]?.contains(colIndexToCheck) == true) {
                        return Pair(rowIndexToCheck, colIndexToCheck)
                    }
                }
            }
        }
        return null
    }

    private fun hasSymbolAdjacent(rowIndex: Int, colIndex: Int): Boolean {
        return hasSymbolDown(rowIndex, colIndex)
                || hasSymbolUp(rowIndex, colIndex)
                || hasSymbolLeft(rowIndex, colIndex)
                || hasSymbolRight(rowIndex, colIndex)
    }


    private fun isInGrid(rowIndex: Int, colIndex: Int): Boolean {
        return rowIndex in 0..maxRow && colIndex in 0..maxCol
    }


    private fun hasSymbolUp(rowIndex: Int, colIndex: Int): Boolean {
        if (isInGrid(rowIndex - 1, colIndex)) {
            val char = engineSchematic[rowIndex -1][colIndex]
            if (isProbablyASymbol(char)) {
                return true
            }
            // Try left and right of that
            return hasSymbolLeft(rowIndex-1, colIndex) || hasSymbolRight(rowIndex-1, colIndex)
        }
        return false
    }

    private fun hasSymbolDown(rowIndex: Int, colIndex: Int): Boolean {
        if (isInGrid(rowIndex+1, colIndex)) {
            val char = engineSchematic[rowIndex +1][colIndex]
            if (isProbablyASymbol(char)) {
                return true
            }
            // Try left and right of that
            return hasSymbolLeft(rowIndex+1, colIndex) || hasSymbolRight(rowIndex+1, colIndex)
        }
        return false
    }

    private fun hasSymbolLeft(rowIndex: Int, colIndex: Int): Boolean {
        if (isInGrid(rowIndex, colIndex -1)) {
            val char = engineSchematic[rowIndex][colIndex -1]
            return isProbablyASymbol(char)
        }
        return false
    }

    private fun hasSymbolRight(rowIndex: Int, colIndex: Int): Boolean {
        if (isInGrid(rowIndex, colIndex +1)) {
            val char = engineSchematic[rowIndex ][colIndex +1]
            return isProbablyASymbol(char)
        }
        return false
    }

    private fun isProbablyASymbol(char: Char): Boolean {
        return char != '.' && !char.isLetterOrDigit()
    }

}