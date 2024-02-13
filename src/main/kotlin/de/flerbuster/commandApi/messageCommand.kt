package de.flerbuster.commandApi

import de.flerbuster.commandApi.command.builders.MessageCommandBuilder
import de.flerbuster.commandApi.command.commands.MessageCommand
import dev.kord.core.Kord
import kotlinx.coroutines.DelicateCoroutinesApi

/**
 * Creates a [MessageCommand] with the given [builder].
 *
 * @param prefix The prefix of the command.
 * @param name The name of the command.
 * @param description The description of the command.
 * @param kord The [Kord] instance.
 * @param register Whether to register the command.
 * @param builder The builder of the command.
 * @return an instance of [MessageCommand].
 */
@DelicateCoroutinesApi
fun messageCommand(
    prefix: String,
    name: String,
    description: String,
    kord: Kord,
    register: Boolean = true,
    builder: MessageCommandBuilder.() -> Unit
): MessageCommand {
    val command = MessageCommandBuilder(prefix, name.lowercase() , description, kord).apply(builder).build()
    if (register) command.register()
    return command
}
