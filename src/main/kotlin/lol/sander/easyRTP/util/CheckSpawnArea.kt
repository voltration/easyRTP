package lol.sander.easyRTP.util

import org.bukkit.Location
import org.bukkit.Material
import org.bukkit.block.Biome
import org.bukkit.block.data.Waterlogged
import org.bukkit.entity.Player

val oceanBiomes: Set<Biome> = setOf(
    Biome.OCEAN,
    Biome.DEEP_OCEAN,
    Biome.FROZEN_OCEAN,
    Biome.WARM_OCEAN,
    Biome.LUKEWARM_OCEAN,
    Biome.COLD_OCEAN,
    Biome.DEEP_FROZEN_OCEAN,
    Biome.DEEP_LUKEWARM_OCEAN,
    Biome.DEEP_COLD_OCEAN
)

fun checkSpawnArea(p: Player, x: Int, y: Int, z: Int): Boolean {
    val block = p.world.getBlockAt(x, y + 1, z)
    val biome = p.world.getBiome(x, y  + 1, z)

    val isBlockWater = when (block.type) {
        Material.WATER -> true
        Material.BUBBLE_COLUMN -> true
        else -> false
    }

    if (isBlockWater) return false
    if (block.blockData is Waterlogged) return false
    if (oceanBiomes.contains(biome)) return false

    return true
}