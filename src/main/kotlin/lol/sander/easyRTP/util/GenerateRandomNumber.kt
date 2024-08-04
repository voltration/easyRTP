package lol.sander.easyRTP.util

import lol.sander.easyRTP.plugin
import kotlin.random.Random

private val min = plugin.config.getString("minrange")?.toIntOrNull() ?: 50_000
private val max = plugin.config.getString("maxrange")?.toIntOrNull() ?: 50_000

fun generateRandomNumber(): Int {
    val randomNumber = Random.nextInt(min, max + 1)
    return if (Random.nextBoolean()) randomNumber else -randomNumber
}