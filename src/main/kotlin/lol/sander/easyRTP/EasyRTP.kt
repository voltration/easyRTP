package lol.sander.easyRTP

import lol.sander.easyRTP.commands.EasyRTP
import lol.sander.easyRTP.commands.RTP
import org.bukkit.plugin.java.JavaPlugin

class EasyRTP : JavaPlugin() {

    override fun onEnable() {
        saveDefaultConfig()
        this.getCommand("rtp")?.setExecutor(RTP(this))
        this.getCommand("easyrtp")?.setExecutor(EasyRTP())
    }

    override fun onDisable() {
    }
}
