package org.example.telegram.bot


import org.telegram.telegrambots.bots.TelegramLongPollingBot
import org.telegram.telegrambots.meta.api.objects.Update
import org.telegram.telegrambots.meta.api.methods.send.SendMessage
import org.telegram.telegrambots.meta.TelegramBotsApi
import org.telegram.telegrambots.updatesreceivers.DefaultBotSession
import java.net.HttpURLConnection
import java.net.URL



class MyTelegramBot : TelegramLongPollingBot() {

    private val botToken = BotConfigToken.botToken.toString()
    private val botUsername = BotConfigToken.botUsername.toString()

    override fun getBotToken(): String = botToken

    override fun getBotUsername(): String = botUsername

    override fun onUpdateReceived(update: Update) {
        if (update.hasMessage() && update.message.hasText()) {
            val chatId = update.message.chatId.toString()
            val receivedMessage = update.message.text

            when (receivedMessage) {
                "/start" -> sendWelcomeMessage(chatId)
                else -> sendTapCount(chatId, receivedMessage)
            }
        }
    }

    private fun sendWelcomeMessage(chatId: String) {
        val message = SendMessage()
        message.chatId = chatId
        message.text = "Welcome! Tap the screen to start the game. Send any message to see your tap count."
        execute(message)
    }

    private fun sendTapCount(chatId: String, taps: String) {
        val message = SendMessage()
        message.chatId = chatId
        message.text = "You have tapped the screen $taps times."
        execute(message)
    }
}

fun sendGetRequest(url: String): String {
    val connection = URL(url).openConnection() as HttpURLConnection
    connection.requestMethod = "GET"

    return try {
        connection.inputStream.bufferedReader().use { it.readText() }
    } finally {
        connection.disconnect()
    }
}

fun main() {
    val botToken = BotConfigToken.botToken.toString()

    try {
        // Delete the webhook if it's set
        val deleteWebhookUrl = "https://api.telegram.org/bot$botToken/deleteWebhook"
        sendGetRequest(deleteWebhookUrl)

        val botsApi = TelegramBotsApi(DefaultBotSession::class.java)
        botsApi.registerBot(MyTelegramBot())
    } catch (e: Exception) {
        // fuck let me try this shit again
        if (e.message?.contains("Error deleting webhook") == true) {
            println("Webhook was not set, proceeding with long polling...")
            val botsApi = TelegramBotsApi(DefaultBotSession::class.java)
            botsApi.registerBot(MyTelegramBot())
        } else {
            e.printStackTrace()
        }
    }
}
