package de.flerbuster.commandapi.builder

import de.flerbuster.commandapi.builder.command.builders.CommandBuilder
import de.flerbuster.commandapi.builder.command.commands.SlashCommand
import de.flerbuster.commandapi.builder.command.builders.SlashCommandBuilder
import dev.kord.core.Kord
import kotlinx.coroutines.DelicateCoroutinesApi

@DelicateCoroutinesApi
fun slashCommand(
    name: String,
    description: String,
    kord: Kord,
    register: Boolean = true,
    builder: CommandBuilder<SlashCommand>.() -> Unit
): SlashCommand {
    val command = SlashCommandBuilder(name.lowercase(), description.lowercase(), kord).apply(builder).build()
    if (register) command.add()
    return command
}