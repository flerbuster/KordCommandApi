package de.flerbuster.commandApi.command.builders

import de.flerbuster.commandApi.command.commands.SlashCommand
import dev.kord.core.Kord

class SlashCommandBuilder(
    override val name: String,
    override val description: String,
    override val kord: Kord
) : CommandBuilder<SlashCommand>(name, description, kord) {
    override fun build() = SlashCommand(name, description, arguments, execution, kord)
}