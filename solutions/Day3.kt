package solutions

import Utils.FileUtils
import Utils.StringUtils

class Day3: Solution {
    companion object {
        var engineSchematic:List<CharArray> = listOf()
        var maxCol: Int = 0
        var maxRow: Int = 0
    }
    override fun solve(inputPath: String) {
        val input = FileUtils().readLineSequence(inputPath)
        engineSchematic = StringUtils().buildMatrix(input.toList())
        maxRow = engineSchematic.size - 1
        maxCol = engineSchematic[0].size -1

        val allNumbers = mutableListOf<Int>()
        var colIndex = 0
        for (rowIndex in engineSchematic.indices) {
            val foundNumbersNearSymbols = mutableListOf<Char>()
            var includeNumber = false
            while (colIndex < maxCol) {
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
                }


                if (includeNumber) {
                    val number = StringUtils().tryParseInt(foundNumbersNearSymbols.joinToString("")) ?: 0
                    allNumbers.add(number)
                    includeNumber = false
                }
                foundNumbersNearSymbols.clear()

                colIndex++
            }
            colIndex = 0
        }

        val resultA = allNumbers.sumOf { it }
        println("ResultA $resultA")
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