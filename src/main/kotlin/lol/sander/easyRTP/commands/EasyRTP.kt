package lol.sander.easyRTP.commands

import lol.sander.easyRTP.EasyRTP
import org.bukkit.command.Command
import org.bukkit.command.CommandExecutor
import org.bukkit.command.CommandSender

class EasyRTP(private val plugin: EasyRTP) : CommandExecutor {
    override fun onCommand(
        sender: CommandSender,
        cmd: Command,
        label: String,
        args: Array<out String>): Boolean {

        sender.sendMessage(plugin.format("&ehttps://github.com/voltration/easyRTP"))
        return true
    }
}