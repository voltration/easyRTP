package lol.sander.easyRTP.commands

import lol.sander.easyRTP.plugin
import lol.sander.easyRTP.util.formatMessage
import org.bukkit.command.Command
import org.bukkit.command.CommandExecutor
import org.bukkit.command.CommandSender

class EasyRTPCommand : CommandExecutor {

    private val spawnPlatformDecayEnabled = plugin.config.getBoolean("spawnPlatformDecayEnabled")
    private val spawnPlatformDecayTime = plugin.config.getInt("spawnPlatformDecayTime")
    private val dontSpawnInWater = plugin.config.getBoolean("dontSpawnInWater")
    private val getHighestBlock = plugin.config.getBoolean("getHighestBlock")
    private val spawnPlatform = plugin.config.getBoolean("spawnPlatform")
    private val spawnPlatformSize = plugin.config.getInt("spawnPlatformSize")
    private val enablePrefix = plugin.config.getBoolean("enablePrefix")
    private val prefix = plugin.config.getString("prefix")
    private val tpDelay = plugin.config.getInt("tpDelay")
    private val min = plugin.config.getInt("minRange")
    private val max = plugin.config.getInt("maxRange")

    private fun f(arg: String): String {
        return formatMessage(arg, true)
    }

    override fun onCommand(sender: CommandSender, cmd: Command, label: String, args: Array<out String>): Boolean {
        sender.sendMessage(f("&d&lEasyRTP &7&o( https://github.com/voltration/easyRTP )"))
        sender.sendMessage(f("&f"))
        sender.sendMessage(f("&fspawnPlatformSize: &e${spawnPlatformSize}x${spawnPlatformSize}"))
        sender.sendMessage(f("&fspawnPlatformDecayEnabled: ${formatAnswer(spawnPlatformDecayEnabled)}"))
        sender.sendMessage(f("&fdontSpawnInWater: ${formatAnswer(dontSpawnInWater)}"))
        sender.sendMessage(f("&fgetHighestBlock: ${formatAnswer(getHighestBlock)}"))
        sender.sendMessage(f("&fspawnPlatform: ${formatAnswer(spawnPlatform)}"))
        sender.sendMessage(f("&fenablePrefix: ${formatAnswer(enablePrefix)}"))
        sender.sendMessage(f("&fspawnPlatformDecayTime: &e$spawnPlatformDecayTime"))
        sender.sendMessage(f("&fprefix: $prefix"))
        sender.sendMessage(f("&ftpDelay: &e${tpDelay}s"))
        sender.sendMessage(f("&fmaxRange: &e$max"))
        sender.sendMessage(f("&fminRange: &e$min"))

        return true
    }

    private fun formatAnswer(flag: Boolean): String {
        return if (flag) "&a[True]" else "&c[False]"
    }
}