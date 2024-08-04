package lol.sander.easyRTP.util

import lol.sander.easyRTP.plugin

fun getMessageString(configField: String): String {
    val errorMessage = "&cWarning: No message for ${configField}."

    return formatMessage(plugin.config.getString(configField) ?: errorMessage)
}