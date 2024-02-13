package de.flerbuster.commandApi.command.builders

import de.flerbuster.commandApi.command.commands.MessageCommand
import de.flerbuster.commandApi.command.options.MessageCommandOptions
import dev.kord.core.Kord
import dev.kord.core.entity.Message

/**
 * Builder for class [MessageCommand].
 */
class MessageCommandBuilder(
    private val prefix: String,
    override val name: String,
    override val description: String,
    override val kord: Kord
) : CommandBuilder<MessageCommand,  Message, MessageCommandOptions>(name, description, kord) {
    var triggeredByBots = false

    override fun build() = MessageCommand(prefix, name, description, execution, arguments, kord, triggeredByBots)
}