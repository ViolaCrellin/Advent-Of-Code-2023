import Solutions.Solution
import java.nio.file.Paths

fun main(){
    val day = 1
    val solution = instantiateSolution(day) ?: throw Exception("day $day not recognized")
    val path = "Inputs\\Day$day"
    solution.solve(path)
}

fun instantiateSolution(day: Int): Solution? {
    return try {
        val className = "Solutions.Day$day"
        val clazz = Class.forName(className)
        clazz.getDeclaredConstructor().newInstance() as? Solution
    } catch (e: Exception) {
        null
    }
}