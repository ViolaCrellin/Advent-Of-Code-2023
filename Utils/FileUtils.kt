package Utils

import java.io.File
import java.nio.charset.Charset

class FileUtils {
    fun readLineSequence(path: String, charset: Charset = Charsets.UTF_8): Sequence<String> {
        println("Current working directory: " + File("").absolutePath)
        return File(path).bufferedReader(charset).lineSequence()
    }
}