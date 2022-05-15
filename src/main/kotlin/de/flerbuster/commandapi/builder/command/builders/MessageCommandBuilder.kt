package de.flerbuster.commandapi.builder.command.builders

import de.flerbuster.commandapi.builder.command.commands.MessageCommand
import dev.kord.core.Kord

class MessageCommandBuilder(
    override val name: String,
    override val description: String,
    override val kord: Kord
) : CommandBuilder<MessageCommand>(name, description, kord) {
    override fun build() = MessageCommand(name, description, execution, onInit, arguments, kord)
}