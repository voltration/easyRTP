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
    private val delay = plugin.config.getString("tpdelay")?.toIntOrNull() ?: 5

    private fun teleportPlayerAsync(p: Player) {
        p.sendMessage(getMessageString("teleportingMessage").replace("{seconds}", delay.toString()))

        plugin.server.scheduler.runTaskAsynchronously(plugin, Runnable {
            var validLocation = false
            var location: Location

            while (!validLocation) {
                val (x, z) = generateRandomNumber() to generateRandomNumber()
                val y = p.world.getHighestBlockYAt(x, z)
                location = Location(p.world, x.toDouble(), (y + 1).toDouble(), z.toDouble())

                validLocation = if (dontSpawnInWater) {
                    checkSpawnArea(p, x, y, z)
                } else {
                    true
                }

                if (validLocation) {
                    plugin.server.scheduler.runTaskLater(plugin, Runnable {
                        p.teleport(location)
                    }, delay * 20L)
                }
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
