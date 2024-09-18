package lol.sander.easyRTP.util

import lol.sander.easyRTP.plugin
import org.bukkit.ChatColor
import java.util.regex.Pattern

private val prefix = plugin.config.getString("prefix")
private val enablePrefix = plugin.config.getBoolean("enablePrefix")
private val hexPattern: Pattern = Pattern.compile("#[a-fA-F0-9]{6}")

fun formatMessage(msg: String, disablePrefix: Boolean = false): String {
    var result = msg
    val matcher = hexPattern.matcher(result)

    while (matcher.find()) {
        val color = result.substring(matcher.start(), matcher.end())
        result = result.replace(color, net.md_5.bungee.api.ChatColor.of(color).toString())
    }
    if (enablePrefix && !disablePrefix) {
        return ChatColor.translateAlternateColorCodes('&', "$prefix&r $result")
    }
    return ChatColor.translateAlternateColorCodes('&', result)
}