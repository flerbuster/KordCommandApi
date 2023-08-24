package de.flerbuster.commandApi.command.commands

import de.flerbuster.commandApi.command.arguments.argument.Argument
import dev.kord.core.Kord
import dev.kord.core.entity.Entity
import kotlinx.coroutines.launch

sealed class Command<T : Command<T>> (
    open val name: String,
    open val description: String,
    open val arguments: MutableList<Argument<*>>,
    private val kord: Kord
) {
    internal lateinit var command: Entity
    val commandId get() = command.id

    abstract val commandName: String

    abstract suspend fun init()

    final fun register() = kord.launch {
        init()
    }
}