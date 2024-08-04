package lol.sander.easyRTP.util

import org.bukkit.entity.Player

fun generateRandomCoordinates(p: Player): Triple<Double, Double, Double> {
    val (x, z) = generateRandomNumber() to generateRandomNumber()
    val y = p.world.getHighestBlockYAt(x, z)

    return Triple(x.toDouble(), y.toDouble(), z.toDouble())
}