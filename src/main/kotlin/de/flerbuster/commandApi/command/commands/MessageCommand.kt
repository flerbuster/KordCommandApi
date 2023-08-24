package de.flerbuster.commandApi.command.commands

import de.flerbuster.commandApi.command.arguments.argument.Argument
import de.flerbuster.commandApi.command.options.MessageCommandOptions
import dev.kord.core.Kord
import dev.kord.core.entity.Message
import dev.kord.core.event.message.MessageCreateEvent
import dev.kord.core.on

class MessageCommand(
    var prefix: String,
    override val name: String,
    override val description: String,
    val execution: suspend (message: Message, options: MessageCommandOptions) -> Unit,
    override val arguments: MutableList<Argument<*>>,
    val kord: Kord,
    private val triggeredByBots: Boolean
) : Command<MessageCommand>(name, description, arguments, kord) {
    override val commandName: String get() = "$prefix$name"
    override suspend fun init() {
        println("creating message command $name")

        kord.on<MessageCreateEvent>(kord) {
            if (message.author?.isBot == true && !triggeredByBots) return@on
            if (!message.content.startsWith(commandName)) return@on
            execution(message, MessageCommandOptions(message, this@MessageCommand))
        }
    }
}