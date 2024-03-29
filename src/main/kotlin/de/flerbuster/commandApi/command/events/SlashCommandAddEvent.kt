package de.flerbuster.commandApi.command.events

import de.flerbuster.commandApi.command.commands.SlashCommand
import dev.kord.common.annotation.KordPreview
import dev.kord.core.Kord

data class SlashCommandAddEvent @KordPreview constructor(
    override val command: SlashCommand,
    override val kord: Kord = command.kord,
    override val customContext: Any? = null,
    override val shard: Int = 0
): CommandAddEvent<SlashCommand>