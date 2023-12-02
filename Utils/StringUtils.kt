package Utils

class StringUtils {

    fun tryParseInt(input: String): Int? {
        return "\\d+".toRegex().find(input)?.value?.toIntOrNull()
    }
}