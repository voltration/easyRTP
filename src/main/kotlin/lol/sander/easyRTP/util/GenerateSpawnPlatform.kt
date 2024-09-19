package lol.sander.easyRTP.util

import lol.sander.easyRTP.plugin
import org.bukkit.Location
import org.bukkit.Material
import org.bukkit.entity.Player

private val block = Material.valueOf(plugin.config.getString("spawnPlatformBlock") ?: "BEDROCK")
private val spawnPlatformSize = plugin.config.getInt("spawnPlatformSize")
private val delay = plugin.config.getInt("spawnPlatformDecayTime") * 20L
private val spawnPlatformDecayEnabled = plugin.config.getBoolean("spawnPlatformDecayEnabled")

fun generateSpawnPlatform(p: Player, x: Int, y: Int, z: Int) {
    val halfSize = spawnPlatformSize / 2

    for (i in -halfSize..halfSize) {
        for (j in -halfSize..halfSize) {
            val loc = Location(p.world, (x + i).toDouble(), (y - 1).toDouble(), (z + j).toDouble())
            val blockAtLoc = p.world.getBlockAt(loc)
            val originalBlockType = blockAtLoc.type
            blockAtLoc.type = block

            if (spawnPlatformDecayEnabled) {
                plugin.server.scheduler.runTaskLater(plugin, Runnable {
                    blockAtLoc.type = originalBlockType
                }, delay)
            }
        }
    }
}
