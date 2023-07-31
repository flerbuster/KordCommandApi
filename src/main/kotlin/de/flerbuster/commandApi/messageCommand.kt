package de.flerbuster.commandApi

import de.flerbuster.commandApi.command.builders.MessageCommandBuilder
import de.flerbuster.commandApi.command.commands.MessageCommand
import dev.kord.core.Kord
import kotlinx.coroutines.DelicateCoroutinesApi

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
