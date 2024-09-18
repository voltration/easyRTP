package lol.sander.easyRTP.commands

import lol.sander.easyRTP.plugin
import lol.sander.easyRTP.util.formatMessage
import org.bukkit.command.Command
import org.bukkit.command.CommandExecutor
import org.bukkit.command.CommandSender

class EasyRTPCommand : CommandExecutor {

    private val dontSpawnInWater = plugin.config.getBoolean("dontSpawnInWater")
    private val getHighestBlock = plugin.config.getBoolean("getHighestBlock")
    private val spawnPlatform = plugin.config.getBoolean("spawnPlatform")
    private val spawnPlatformSize = plugin.config.getInt("spawnPlatformSize")
    private val tpdelay = plugin.config.getInt("tpDelay")
    private val min = plugin.config.getInt("minRange")
    private val max = plugin.config.getInt("maxRange")

    override fun onCommand(sender: CommandSender, cmd: Command, label: String, args: Array<out String>): Boolean {
        sender.sendMessage(formatMessage("&d&lEasyRTP &7&o( https://github.com/voltration/easyRTP )"))
        sender.sendMessage(formatMessage("&f"))
        sender.sendMessage(formatMessage("&fdontSpawnInWater: ${formatAnswer(dontSpawnInWater)}"))
        sender.sendMessage(formatMessage("&fgetHighestBlock: ${formatAnswer(getHighestBlock)}"))
        sender.sendMessage(formatMessage("&fspawnPlatform: ${formatAnswer(spawnPlatform)}"))
        sender.sendMessage(formatMessage("&fspawnPlatformSize: &e${spawnPlatformSize}x${spawnPlatformSize}"))
        sender.sendMessage(formatMessage("&ftpDelay: &e${tpdelay}"))
        sender.sendMessage(formatMessage("&fmaxRange: &e${max}"))
        sender.sendMessage(formatMessage("&fminRange: &e${min}"))

        return true
    }

    private fun formatAnswer(flag: Boolean): String {
        return if (flag) "&a[ True ]" else "&c[ False ]"
    }
}