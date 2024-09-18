package lol.sander.easyRTP.util

import lol.sander.easyRTP.plugin
import kotlin.random.Random

private val min = plugin.config.getInt("minRange")
private val max = plugin.config.getInt("maxRange")

fun generateRandomNumber(): Int {
    return (min..max).random()
}

fun generateRandomHeightCoordinate(): Int {
    return (-64..320).random()
}