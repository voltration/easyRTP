package lol.sander.easyRTP.util

import lol.sander.easyRTP.plugin
import kotlin.random.Random

private val min = plugin.config.getInt("minrage")
private val max = plugin.config.getInt("maxrange")

fun generateRandomNumber(): Int {
    val randomNumber = Random.nextInt(min, max + 1)
    return if (Random.nextBoolean()) randomNumber else -randomNumber
}