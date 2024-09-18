package lol.sander.easyRTP.commands

import lol.sander.easyRTP.plugin
import lol.sander.easyRTP.util.*
import org.bukkit.Location
import org.bukkit.World
import org.bukkit.command.Command
import org.bukkit.command.CommandExecutor
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player

class RTP : CommandExecutor {

    private val dontSpawnInWater = plugin.config.getBoolean("dontSpawnInWater")
    private val getHighestBlock = plugin.config.getBoolean("getHighestBlock")
    private val spawnPlatform = plugin.config.getBoolean("spawnPlatform")
    private val delay = plugin.config.getInt("tpDelay")

    private fun teleportPlayerAsync(p: Player) {
        if (dontSpawnInWater) {
            teleportPlayerInSuitableLocationAsync(p, spawnPlatform)
        }
        else {
            teleportPlayerInRandomLocationAsync(p, spawnPlatform)
        }
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
                location = Location(p.world, x.toDouble(), y.toDouble(), z.toDouble())
            }

            // teleportation has to happen on the main thread
            plugin.server.scheduler.runTaskLater(plugin, Runnable {
                location?.let {
                    generateSpawnPlatform(p, it.blockY)
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
            val location = Location(p.world, x.toDouble(), y.toDouble(), z.toDouble())

            // teleportation has to happen on the main thread
            plugin.server.scheduler.runTaskLater(plugin, Runnable {
                generateSpawnPlatform(p, location.blockY)
                p.teleport(location)
            }, delay * 20L)
        })
    }

    private fun sendMessage(p: Player) {
        p.sendMessage(getMessageString("teleportingMessage").replace("{seconds}", delay.toString()))
    }

    override fun onCommand(sender: CommandSender, cmd: Command, label: String, args: Array<out String>): Boolean {

        if (sender !is Player) {
            sender.sendMessage(getMessageString("playerOnlyMessage"))
            return true
        }

        val p: Player = sender

        if (!p.hasPermission("easyrtp.rtp")) {
            p.sendMessage(getMessageString("noPermissionMessage"))
            return true
        }

        if (p.world.environment != World.Environment.NORMAL) {
            p.sendMessage(getMessageString("worldOnlyMessage"))
            return true
        }

        teleportPlayerAsync(p)
        return true
    }
}
