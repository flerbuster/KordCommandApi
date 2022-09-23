package de.flerbuster.commandApi

import de.flerbuster.commandApi.command.builders.CommandBuilder
import de.flerbuster.commandApi.command.commands.SlashCommand
import de.flerbuster.commandApi.command.builders.SlashCommandBuilder
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
