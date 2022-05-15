package de.flerbuster.commandapi.builder

import de.flerbuster.commandapi.builder.command.builders.CommandBuilder
import de.flerbuster.commandapi.builder.command.commands.MessageCommand
import de.flerbuster.commandapi.builder.command.builders.MessageCommandBuilder
import dev.kord.core.Kord
import kotlinx.coroutines.DelicateCoroutinesApi

@DelicateCoroutinesApi
fun messageCommand(
    name: String,
    description: String,
    kord: Kord,
    register: Boolean = true,
    builder: CommandBuilder<MessageCommand>.() -> Unit
): MessageCommand {
    val command = MessageCommandBuilder(name.lowercase() , description, kord).apply(builder).build()
    if (register) command.add()
    return command
}