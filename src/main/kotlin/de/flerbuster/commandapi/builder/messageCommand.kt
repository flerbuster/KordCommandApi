package commandApi.builder

import commandApi.builder.command.builders.CommandBuilder
import commandApi.builder.command.commands.MessageCommand
import commandApi.builder.command.builders.MessageCommandBuilder
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