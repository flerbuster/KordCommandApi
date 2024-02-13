package de.flerbuster.commandApi

import de.flerbuster.commandApi.command.builders.SlashCommandBuilder
import de.flerbuster.commandApi.command.commands.SlashCommand
import dev.kord.core.Kord
import kotlinx.coroutines.DelicateCoroutinesApi

/**
 * creates [SlashCommand], with the given  [name] and [description]
 *
 * @param name The name of the command.
 * @param description The description of the command.
 * @param kord The [Kord] instance.
 * @param register If `true` the command will be registered.
 * @param recache If `true` the command will be recached.
 * @param builder The [SlashCommandBuilder] to build the command from.
 * @return an instance of [SlashCommand].
 */
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
