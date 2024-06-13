package org.example.telegram.bot

import org.telegram.telegrambots.bots.TelegramLongPollingBot
import org.telegram.telegrambots.meta.api.objects.Update
import org.telegram.telegrambots.meta.api.methods.send.SendMessage
import org.telegram.telegrambots.meta.api.methods.send.SendPhoto
import org.telegram.telegrambots.meta.api.objects.InputFile
import org.telegram.telegrambots.meta.TelegramBotsApi
import org.telegram.telegrambots.updatesreceivers.DefaultBotSession
import java.io.File

class MyTelegramBot : TelegramLongPollingBot() {

    private val botToken = BotConfigToken.botToken.toString()
    private val botUsername = BotConfigToken.botUsername.toString()
    private val sentWelcomeMessages = mutableSetOf<Long>() // Store chat IDs to avoid duplicate welcome messages

    override fun getBotToken(): String = botToken

    override fun getBotUsername(): String = botUsername

    override fun onUpdateReceived(update: Update) {
        if (update.hasMessage() && update.message.hasText()) {
            val chatId = update.message.chatId
            val receivedMessage = update.message.text

            // Send welcome message and image if it's the first message from the user
            if (!sentWelcomeMessages.contains(chatId)) {
                sendWelcomeMessage(chatId)
                sendWelcomeImage(chatId)
                sentWelcomeMessages.add(chatId)
            }

            when (receivedMessage) {
                "/start" -> sendWelcomeMessage(chatId)
                else -> sendTapCount(chatId, receivedMessage)
            }
        }
    }

    private fun sendWelcomeMessage(chatId: Long) {
        val message = SendMessage()
        message.chatId = chatId.toString()
        message.text = "Delighted to have you!!!."

        try {
            execute(message)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private fun sendWelcomeImage(chatId: Long) {
        val photo = SendPhoto()
        photo.chatId = chatId.toString()
        val imagePath = "C:\\Users\\CHINAZA\\IdeaProjects\\SATURN\\src\\main\\resources\\saturn_img.jpg"
        val imageFile = File(imagePath)

        if (imageFile.exists() && imageFile.canRead()) {
            photo.photo = InputFile(imageFile)
            photo.caption = "Welcome to the bot, under heavy construction!"

            try {
                execute(photo)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        } else {
            println("Image file not found or cannot be read: $imagePath")
            if (!imageFile.exists()) {
                println("File does not exist.")
            }
            if (!imageFile.canRead()) {
                println("File cannot be read.")
            }
        }
    }

    private fun sendTapCount(chatId: Long, taps: String) {
        val message = SendMessage()
        message.chatId = chatId.toString()
        message.text = "You have tapped the screen $taps times."

        try {
            execute(message)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
}

fun main() {
    try {
        val botsApi = TelegramBotsApi(DefaultBotSession::class.java)
        botsApi.registerBot(MyTelegramBot())
    } catch (e: Exception) {
        e.printStackTrace()
    }
}

