package de.flerbuster.commandApi.command.commands

import de.flerbuster.commandApi.command.arguments.argument.Argument
import de.flerbuster.commandApi.command.arguments.provider.ArgumentProvider
import de.flerbuster.commandApi.command.events.MessageCommandAddEvent
import de.flerbuster.commandApi.command.options.MessageCommandOptions
import dev.kord.common.annotation.KordPreview
import dev.kord.core.Kord
import dev.kord.core.entity.Message
import dev.kord.core.event.message.MessageCreateEvent
import dev.kord.core.on
import kotlin.reflect.KClass

class MessageCommand(
    var prefix: String,
    override val name: String,
    override val description: String,
    val execution: suspend ArgumentProvider.(message: Message, options: MessageCommandOptions) -> Unit,
    override val arguments: MutableList<Argument<*>>,
    private val exceptionHandling: HashMap<KClass<Exception>, suspend (
        exception: Exception,
        interaction: Message,
        command: MessageCommand
    ) -> Unit>,
    val kord: Kord,
    private val triggeredByBots: Boolean
) : Command<MessageCommand>(name, description, arguments, kord) {
    override val commandName: String get() = "$prefix$name"

    /**
     * initializes the command, listening for messages with the command name
     */
    @OptIn(KordPreview::class)
    override suspend fun init() {
        kord.on<MessageCreateEvent>(kord) {
            if (message.author?.isBot == true && !triggeredByBots) return@on
            if (!message.content.startsWith(commandName)) return@on
            MessageCommandOptions(message, this@MessageCommand) { options ->
                try {
                    with(ArgumentProvider(options)) {
                        execution(message, options)
                    }
                } catch (e: Exception) {
                    exceptionHandling.forEach { (type, handler) ->
                        if (type.isInstance(e)) {
                            handler(
                                e,
                                message,
                                this@MessageCommand
                            )
                        }
                    }
                }
            }
        }

        emitEvent(MessageCommandAddEvent(this))
    }
}