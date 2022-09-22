package de.flerbuster.commandApi.builder.command.builders

import de.flerbuster.commandApi.builder.command.commands.MessageCommand
import dev.kord.core.Kord

class MessageCommandBuilder(
    override val name: String,
    override val description: String,
    override val kord: Kord
) : CommandBuilder<MessageCommand>(name, description, kord) {
    override fun build() = MessageCommand(name, description, execution, arguments, kord)
}