package lol.sander.easyRTP.commands

import lol.sander.easyRTP.EasyRTP
import org.bukkit.Location
import org.bukkit.World
import org.bukkit.command.Command
import org.bukkit.command.CommandExecutor
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player
import kotlin.random.Random

class RTP(private val plugin: EasyRTP) : CommandExecutor {

    private val min = plugin.getConfigValue("minrange")?.toIntOrNull() ?: 50_000
    private val max = plugin.getConfigValue("maxrange")?.toIntOrNull() ?: 50_000
    private val delay = plugin.getConfigValue("tpdelay")?.toIntOrNull() ?: 5

    private val worldOnlyMessage = plugin.format(
        plugin.getConfigValue("worldOnlyMessage")
            ?: "&e/RTP &fis only allowed in the overworld.")

    private val teleportingMessage = plugin.format(
        plugin.getConfigValue("teleportingMessage")
            ?: "&fTeleporting in &e{seconds} &fseconds..."
    ).replace("{seconds}", delay.toString())

    private val noPermissionMessage = plugin.format(
        plugin.getConfigValue("noPermissionMessage")
            ?: "&eYou do not have the &eeasyrtp.rtp &fpermisison.")

    private fun generateRandomNumber() = Random.nextInt(min, max + 1).let {
        if (Random.nextBoolean()) it else -it
    }

    private fun teleportPlayerAsync(p: Player) {
        p.sendMessage(teleportingMessage)

        plugin.server.scheduler.runTaskAsynchronously(plugin, Runnable {
            val (x, z) = generateRandomNumber() to generateRandomNumber()
            val y = p.world.getHighestBlockYAt(x, z)
            val location = Location(p.world, x.toDouble(), (y + 1).toDouble(), z.toDouble())

            plugin.server.scheduler.runTask(plugin, Runnable {
                p.teleport(location)
            })
        })
    }

    override fun onCommand(
        sender: CommandSender,
        cmd: Command,
        label: String,
        args: Array<out String>
    ): Boolean {

        (sender as? Player)?.let { p ->
            if (p.hasPermission("easyrtp.rtp")) {
                when (p.world.environment) {
                    World.Environment.NORMAL -> teleportPlayerAsync(p)
                    else -> p.sendMessage(worldOnlyMessage)
                }
            }
            else {
                p.sendMessage(noPermissionMessage)
            }
        }
        return true
    }
}