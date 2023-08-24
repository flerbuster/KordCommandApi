package de.flerbuster.commandApi

import de.flerbuster.commandApi.command.builders.SlashCommandBuilder
import de.flerbuster.commandApi.command.commands.SlashCommand
import dev.kord.core.Kord
import kotlinx.coroutines.DelicateCoroutinesApi

@DelicateCoroutinesApi
fun slashCommand(
    name: String,
    description: String,
    kord: Kord,
    register: Boolean = true,
    recache: Boolean = false,
    builder: SlashCommandBuilder.() -> Unit
): SlashCommand {
    val command = SlashCommandBuilder(name.lowercase(), description.lowercase(), kord, recache).apply(builder).build()
    if (register) command.register()
    return command
}
