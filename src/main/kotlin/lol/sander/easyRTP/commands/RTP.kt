package lol.sander.easyRTP.commands

import lol.sander.easyRTP.EasyRTP
import lol.sander.easyRTP.util.*
import org.bukkit.Location
import org.bukkit.World
import org.bukkit.command.Command
import org.bukkit.command.CommandExecutor
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player

class RTP(private val plugin: EasyRTP) : CommandExecutor {

    private val dontSpawnInWater = plugin.config.getBoolean("dontSpawnInWater")
    private val delay = plugin.config.getInt("tpdelay")

    private fun teleportPlayerAsync(p: Player) {
        p.sendMessage(getMessageString("teleportingMessage").replace("{seconds}", delay.toString()))

        if (!dontSpawnInWater) {
            val (x, y, z) = generateRandomCoordinates(p)

            plugin.server.scheduler.runTaskLater(plugin, Runnable {
                p.teleport(Location(p.world, x, y, z))
            }, delay * 20L)

            return
        }

        plugin.server.scheduler.runTaskAsynchronously(plugin, Runnable {
            var validLocation = false
            var location: Location? = null

            while (!validLocation) {
                val (x, y, z) = generateRandomCoordinates(p)
                validLocation = checkSpawnArea(p, x.toInt(), y.toInt(), z.toInt())
                location = Location(p.world, x, y, z)
            }

            if (location != null) {
                plugin.server.scheduler.runTaskLater(plugin, Runnable {
                    p.teleport(location)
                }, delay * 20L)
            }
        })
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
