package lol.sander.easyRTP.commands

import lol.sander.easyRTP.plugin
import lol.sander.easyRTP.util.*
import org.bukkit.Location
import org.bukkit.Sound
import org.bukkit.World
import org.bukkit.command.Command
import org.bukkit.command.CommandExecutor
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player
import java.util.*
import kotlin.collections.HashMap

class RTP : CommandExecutor {
    private val cooldowns: HashMap<UUID, Long> = HashMap()
    private val cooldown = plugin.config.getInt("cooldown")
    private val dontSpawnInWater = plugin.config.getBoolean("dontSpawnInWater")
    private val getHighestBlock = plugin.config.getBoolean("getHighestBlock")
    private val spawnPlatform = plugin.config.getBoolean("spawnPlatform")
    private val playSoundOnCountdown = plugin.config.getBoolean("playSoundOnCountdown")
    private val sound = Sound.valueOf(plugin.config.getString("countdownSound") ?: "BLOCK_NOTE_BLOCK_PLING")
    private val delay = plugin.config.getInt("tpDelay")

    private fun teleportPlayerAsync(p: Player) {
        if (dontSpawnInWater) teleportPlayerInSuitableLocationAsync(p, spawnPlatform)
        else teleportPlayerInRandomLocationAsync(p, spawnPlatform)
    }

    private fun teleportPlayerInSuitableLocationAsync(p: Player, platform: Boolean) {
        var validLocation = false
        var location: Location? = null

        sendMessage(p)

        // offload computing to a worker
        plugin.server.scheduler.runTaskAsynchronously(plugin, Runnable {
            while (!validLocation) {
                val (x, z) = generateRandomCoordinates()
                val y = if (getHighestBlock) p.world.getHighestBlockYAt(x, z) else generateRandomHeightCoordinate()

                validLocation = checkSpawnArea(p, x, y, z)

                if (validLocation) {
                    location = Location(p.world, x.toDouble() + 0.5, y.toDouble() + 1, z.toDouble() + 0.5)
                }
            }

            // teleportation has to happen on the main thread
            plugin.server.scheduler.runTaskLater(plugin, Runnable {
                location?.let {
                    if (platform) generateSpawnPlatform(p, it.blockX, it.blockY, it.blockZ)
                    p.teleport(it)
                }
            }, delay * 20L)
        })
    }

    private fun teleportPlayerInRandomLocationAsync(p: Player, platform: Boolean) {

        sendMessage(p)

        // offload computing to a worker
        plugin.server.scheduler.runTaskAsynchronously(plugin, Runnable {
            val (x, z) = generateRandomCoordinates()
            val y = if (getHighestBlock) p.world.getHighestBlockYAt(x, z) else generateRandomHeightCoordinate()
            val location = Location(p.world, x.toDouble() + 0.5, y.toDouble() + 1, z.toDouble() + 0.5)

            // teleportation has to happen on the main thread
            plugin.server.scheduler.runTaskLater(plugin, Runnable {
                if (platform) generateSpawnPlatform(p, x, y, z)
                p.teleport(location)
            }, delay * 20L)
        })
    }

    private fun sendMessage(p: Player) {
        var countdown = delay
        plugin.server.scheduler.runTaskTimer(plugin, Runnable {
            if (countdown > 0) {
                if (playSoundOnCountdown) p.playSound(p.location, sound, 1.0F, 1.0F)
                p.sendMessage(
                    getMessageString("teleportingMessage").replace("{seconds}", countdown.toString()))
                    countdown--
            }

            plugin.server.scheduler.cancelTask(this.hashCode())
        }, 0L, 20L)
    }

    private fun setCooldown(p: Player) {
        cooldowns[p.uniqueId] = System.currentTimeMillis() + (cooldown * 1000)
    }

    override fun onCommand(sender: CommandSender, cmd: Command, label: String, args: Array<out String>): Boolean {

        if (sender !is Player) {
            sender.sendMessage(getMessageString("playerOnlyMessage"))
            return true
        }

        val p: Player = sender
        val cooldownEnd = cooldowns[p.uniqueId]
        val currentTime = System.currentTimeMillis()

        if (!p.hasPermission("easyrtp.rtp")) {
            p.sendMessage(getMessageString("noPermissionMessage"))
            return true
        }

        if (p.world.environment != World.Environment.NORMAL) {
            p.sendMessage(getMessageString("worldOnlyMessage"))
            return true
        }

        if (cooldownEnd != null && currentTime < cooldownEnd) {
            val remainingTime = (cooldownEnd - currentTime) / 1000
            p.sendMessage(getMessageString("cooldownMessage").replace("{seconds}", remainingTime.toString()))
            return true
        }

        setCooldown(p)
        teleportPlayerAsync(p)
        return true
    }
}
