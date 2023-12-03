package Utils

class StringUtils {

    fun tryParseInt(input: String): Int? {
        return "\\d+".toRegex().find(input)?.value?.toIntOrNull()
    }

    fun buildMatrix(input: List<String>): List<CharArray> {
        return input.map { it.toCharArray() }
    }
}