package de.flerbuster.commandApi.command.builders

import de.flerbuster.commandApi.command.commands.SlashCommand
import de.flerbuster.commandApi.command.options.SlashCommandOptions
import dev.kord.core.Kord
import dev.kord.core.entity.interaction.ChatInputCommandInteraction

class SlashCommandBuilder(
    override val name: String,
    override val description: String,
    override val kord: Kord,
    private val recache: Boolean = false
) : CommandBuilder<SlashCommand, ChatInputCommandInteraction, SlashCommandOptions>(name, description, kord) {
    override fun build() = SlashCommand(name, description, arguments, execution, exceptionHandlers, kord, recache)
}