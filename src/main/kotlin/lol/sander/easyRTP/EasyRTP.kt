package lol.sander.easyRTP

import lol.sander.easyRTP.commands.RTP
import org.bukkit.ChatColor
import org.bukkit.plugin.java.JavaPlugin

class EasyRTP : JavaPlugin() {

    override fun onEnable() {
        saveDefaultConfig()
        this.getCommand("rtp")?.setExecutor(RTP(this))
    }

    override fun onDisable() {
    }

    fun getConfigValue(key: String): String? = config.getString(key)
    fun format(msg: String): String = ChatColor.translateAlternateColorCodes('&', msg)
}
