package lol.sander.easyRTP.util

import lol.sander.easyRTP.EasyRTP

class GetString(private val plugin: EasyRTP) {

    private val formatter = Format()

    fun getString(configField: String): String {
        val errorMessage = "&cWarning: No message for ${configField}."

        return formatter.format(plugin.config.getString(configField) ?: errorMessage)
    }

}