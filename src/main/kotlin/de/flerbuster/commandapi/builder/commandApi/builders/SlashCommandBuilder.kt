package commandApi.builder.command.builders

import commandApi.builder.command.builders.CommandBuilder
import commandApi.builder.command.commands.SlashCommand
import dev.kord.core.Kord

class SlashCommandBuilder(
    override val name: String,
    override val description: String,
    override val kord: Kord
) : CommandBuilder<SlashCommand>(name, description, kord) {
    override fun build() = SlashCommand(name, description, arguments, execution, onInit, kord)
}