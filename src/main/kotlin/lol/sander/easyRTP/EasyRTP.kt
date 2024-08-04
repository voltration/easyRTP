package lol.sander.easyRTP

import lol.sander.easyRTP.commands.EasyRTPCommand
import lol.sander.easyRTP.commands.RTP
import org.bukkit.plugin.java.JavaPlugin

lateinit var plugin: EasyRTP
    private set

class EasyRTP : JavaPlugin() {
    init {
        plugin = this
    }

    override fun onEnable() {
        saveDefaultConfig()
        this.getCommand("rtp")?.setExecutor(RTP(this))
        this.getCommand("easyrtp")?.setExecutor(EasyRTPCommand())
    }

    override fun onDisable() {
    }
}
