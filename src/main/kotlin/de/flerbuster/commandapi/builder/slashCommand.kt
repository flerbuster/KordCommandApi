package commandApi.builder

import commandApi.builder.command.builders.CommandBuilder
import commandApi.builder.command.commands.SlashCommand
import commandApi.builder.command.builders.SlashCommandBuilder
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