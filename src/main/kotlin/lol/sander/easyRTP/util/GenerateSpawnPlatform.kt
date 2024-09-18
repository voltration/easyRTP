package lol.sander.easyRTP.util

import lol.sander.easyRTP.plugin
import org.bukkit.Location
import org.bukkit.Material
import org.bukkit.entity.Player

private val spawnPlatformSize = plugin.config.getInt("spawnPlatformSize")
private val block = Material.BEDROCK

fun generateSpawnPlatform(p: Player, x: Int, y: Int, z: Int) {

    for (i in 0 until spawnPlatformSize) {
        val loc = Location(p.world, (x + i).toDouble(), y.toDouble(), z.toDouble())
        p.world.getBlockAt(loc).type = block
    }
}