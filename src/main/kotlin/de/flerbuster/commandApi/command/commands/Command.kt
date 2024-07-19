package de.flerbuster.commandApi.command.commands

import de.flerbuster.commandApi.command.arguments.argument.Argument
import dev.kord.core.Kord
import dev.kord.core.entity.Entity
import dev.kord.core.event.Event
import dev.kord.core.on
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.launch

/**
 * Base class for all commands ([SlashCommand], [MessageCommand])
 */
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

    @Suppress("UNCHECKED_CAST")
    suspend fun emitEvent(event: Event) {
        (kord.events as MutableSharedFlow<Event>).emit(event)
    }

    fun register() = kord.launch {
        init()
    }
}