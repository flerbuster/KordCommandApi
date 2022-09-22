package de.flerbuster.commandApi.builder

import de.flerbuster.commandApi.builder.command.builders.CommandBuilder
import de.flerbuster.commandApi.builder.command.commands.Command
import de.flerbuster.commandApi.builder.command.commands.MessageCommand
import de.flerbuster.commandApi.builder.command.commands.SlashCommand
import de.flerbuster.commandApi.builder.errors.TypeNotSupportedError
import dev.kord.core.Kord
import kotlinx.coroutines.DelicateCoroutinesApi

@DelicateCoroutinesApi
@Suppress("UNCHECKED_CAST")
inline fun <reified T: Command<T>> command(
    name: String, 
    description: String,
    kord: Kord, 
    register: Boolean = true, 
    noinline builder: CommandBuilder<T>.() -> Unit
): T {
    return when (T::class) {
        SlashCommand::class -> slashCommand(
            name,
            description,
            kord,
            register,
            builder as CommandBuilder<SlashCommand>.() -> Unit
        ) as T

        MessageCommand::class -> messageCommand(
            name,
            description,
            kord,
            register,
            builder as CommandBuilder<MessageCommand>.() -> Unit
        ) as T

        else -> throw TypeNotSupportedError("type ${T::class.qualifiedName} is no valid command type")
    }
}
