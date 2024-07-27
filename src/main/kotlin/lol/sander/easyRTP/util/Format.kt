package lol.sander.easyRTP.util

import org.bukkit.ChatColor
import java.util.regex.Pattern

class Format {

    private val hexPattern: Pattern = Pattern.compile("#[a-fA-F0-9]{6}")

    fun format(msg: String): String {
        var result = msg
        val matcher = hexPattern.matcher(result)

        while (matcher.find()) {
            val color = result.substring(matcher.start(), matcher.end())
            result = result.replace(color, net.md_5.bungee.api.ChatColor.of(color).toString())
        }
        return ChatColor.translateAlternateColorCodes('&', result)

    }
}