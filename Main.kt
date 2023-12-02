import solutions.Solution

fun main(){
    val day = 2
    val solution = instantiateSolution(day) ?: throw Exception("day $day not recognized")
    val path = "Inputs\\Day$day"
    solution.solve(path)
}

fun instantiateSolution(day: Int): Solution? {
    return try {
        val className = "solutions.Day$day"
        val clazz = Class.forName(className)
        clazz.getDeclaredConstructor().newInstance() as? Solution
    } catch (e: Exception) {
        null
    }
}