package de.flerbuster.exampleBot

import de.flerbuster.commandApi.command.events.CommandAddEvent
import de.flerbuster.commandApi.messageCommand
import de.flerbuster.commandApi.slashCommand
import dev.kord.core.Kord
import dev.kord.core.behavior.interaction.respondEphemeral
import dev.kord.core.behavior.interaction.respondPublic
import dev.kord.core.behavior.reply
import dev.kord.core.entity.User
import dev.kord.core.on
import dev.kord.gateway.Intent
import dev.kord.gateway.PrivilegedIntent
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.runBlocking
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json
import kotlin.random.Random

@Serializable
data class Fler(val type: Int, val fler: String)

class ExceptionThatMightHappen : Exception("fler")

@OptIn(DelicateCoroutinesApi::class, ExperimentalSerializationApi::class)
val flerCommand = slashCommand("fler", "fler", Bot.kord) {
    val json = Json {
        ignoreUnknownKeys = true
        prettyPrint = true
        prettyPrintIndent = " ".repeat(0)
    }

    val userArgument = basicArgument<User>("user", "user who gets flered")

    val flerTypeArgument = customArgument<Fler?>("fler", "type of fler", json) {
        choice("fler1", Fler(1, "das ist hier ist fler 1"))
        choice("fler2", Fler(2, "das ist hier ist fler 2"))
        choice("fler3", Fler(3, "das ist hier ist fler 3"))
    }

    val flerMessageArgument = argument<String?>("flermessage", "fler message")

    runs { interaction, options ->
        val user by userArgument
        val fler by flerTypeArgument.or(Fler(4, "default fler"))
        val message by flerMessageArgument.or("default message")

        if (Random.nextBoolean()) throw ExceptionThatMightHappen()

        interaction.respondPublic {
            content = "hm ${user.mention} wurde mit $fler geflert ($message)"
        }
    }

    catches<ExceptionThatMightHappen> { exception, interaction, command ->
        interaction.respondEphemeral { content = "hm jop exception passiert" }
    }
}

@OptIn(DelicateCoroutinesApi::class)
val flerMessageCommand = messageCommand("!", "fler", "fler", Bot.kord, register = false) {
    basicArgument<User>("user", "user who gets flered") {
        required = true
    }

    argument<Int>("fler", "fler index")

    val flerMessage = argument<String?>("flermessage", "fler message")

    runs { message, options ->
        val user = options.users["user"]!!
        val index = options.integers["fler"]!!
        val flerMsg = flerMessage.get() ?: "default message"

        val flers = listOf(Fler(1, "das ist hier ist fler 1"), Fler(2, "das ist hier ist fler 2"), Fler(3, "das ist hier ist fler 3"))
        val fler = flers[index.toInt()]
        if (Random.nextBoolean()) throw ExceptionThatMightHappen()

        message.reply {
            content = "hm ${user.mention} wurde mit $fler geflert ($flerMsg)"
        }
    }

    catches<ExceptionThatMightHappen> { exception, message, command ->
        message.reply {
            content = "exception passiert"
        }
    }
}

object Bot {
    val kord = runBlocking { Kord("TOKEN") }

    @OptIn(PrivilegedIntent::class)
    suspend fun run() {
        kord.on<CommandAddEvent<*>> {
            println("${command::class.simpleName} ${command.name} registered")
        }

        flerCommand
        flerMessageCommand.register()


        kord.login {
            intents {
                +Intent.MessageContent
            }
        }
    }
}


suspend fun main() {
    Bot.run()
}