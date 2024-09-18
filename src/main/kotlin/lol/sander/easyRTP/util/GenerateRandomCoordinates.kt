package lol.sander.easyRTP.util

fun generateRandomCoordinates(): Pair<Int, Int> {
    val (x, z) = generateRandomNumber() to generateRandomNumber()
    return Pair(x, z)
}