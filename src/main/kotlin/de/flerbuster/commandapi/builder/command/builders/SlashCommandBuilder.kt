package de.flerbuster.commandapi.builder.command.builders

import de.flerbuster.commandapi.builder.command.commands.SlashCommand
import dev.kord.core.Kord

class SlashCommandBuilder(
    override val name: String,
    override val description: String,
    override val kord: Kord
) : CommandBuilder<SlashCommand>(name, description, kord) {
    override fun build() = SlashCommand(name, description, arguments, execution, onInit, kord)
}