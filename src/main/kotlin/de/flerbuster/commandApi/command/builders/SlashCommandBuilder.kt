package de.flerbuster.commandApi.builder.command.builders

import de.flerbuster.commandApi.builder.command.commands.SlashCommand
import dev.kord.core.Kord

class SlashCommandBuilder(
    override val name: String,
    override val description: String,
    override val kord: Kord
) : CommandBuilder<SlashCommand>(name, description, kord) {
    override fun build() = SlashCommand(name, description, arguments, execution, kord)
}