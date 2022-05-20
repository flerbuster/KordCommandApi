package commandApi.builder.command.builders

import commandApi.builder.command.builders.CommandBuilder
import commandApi.builder.command.commands.MessageCommand
import dev.kord.core.Kord

class MessageCommandBuilder(
    override val name: String,
    override val description: String,
    override val kord: Kord
) : CommandBuilder<MessageCommand>(name, description, kord) {
    override fun build() = MessageCommand(name, description, execution, onInit, arguments, kord)
}