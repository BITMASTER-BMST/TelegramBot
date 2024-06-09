package org.example.Network

import org.example.telegram.bot.BotConfigToken
import java.net.HttpURLConnection
import java.net.URL

fun main() {
    val url = URL(BotConfigToken.url.toString())
    try {
        val connection = url.openConnection() as HttpURLConnection
        connection.requestMethod = "GET"
        connection.connectTimeout = 5000
        connection.readTimeout = 5000

        val responseCode = connection.responseCode
        println("Response Code: $responseCode")

        if (responseCode == HttpURLConnection.HTTP_OK) {
            val inputStream = connection.inputStream
            val response = inputStream.bufferedReader().use { it.readText() }
            println(response)
        } else {
            println("GET request failed")
        }
    } catch (e: Exception) {
        e.printStackTrace()
    }
}
