package lol.sander.easyRTP.util

import org.bukkit.Material
import org.bukkit.entity.Player


fun checkSpawnArea(p: Player, x: Int, y: Int, z: Int): Boolean {
    val pos = p.world.getBlockAt(x, y, z)

    return !(pos.type == Material.WATER ||
            pos.type == Material.KELP ||
            pos.type == Material.KELP_PLANT ||
            pos.type == Material.SEAGRASS ||
            pos.type == Material.TALL_SEAGRASS)
}
