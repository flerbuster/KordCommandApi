package de.flerbuster.commandApi.builder

import de.flerbuster.commandApi.builder.command.builders.CommandBuilder
import de.flerbuster.commandApi.builder.command.commands.SlashCommand
import de.flerbuster.commandApi.builder.command.builders.SlashCommandBuilder
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
    if (register) command.register()
    return command
}
