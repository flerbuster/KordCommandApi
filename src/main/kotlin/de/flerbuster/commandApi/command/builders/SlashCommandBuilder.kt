package de.flerbuster.commandApi.command.builders

import de.flerbuster.commandApi.command.commands.SlashCommand
import de.flerbuster.commandApi.command.options.SlashCommandOptions
import dev.kord.core.Kord
import dev.kord.core.entity.interaction.ChatInputCommandInteraction

/**
 * Builder class for [SlashCommand]
 */
class SlashCommandBuilder(
    override val name: String,
    override val description: String,
    override val kord: Kord,
    private val recache: Boolean = false
) : CommandBuilder<SlashCommand, ChatInputCommandInteraction, SlashCommandOptions>(name, description, kord) {
    /**
     * builds the [SlashCommand]
     *
     * @return [SlashCommand]
     */
    override fun build() = SlashCommand(
        name,
        description,
        arguments,
        execution,
        exceptionHandlers,
        nameLocalizations,
        descriptionLocalizations,
        kord,
        recache
    )
}