package de.flerbuster.commandApi.command.commands

import dev.kord.core.Kord
import dev.kord.core.entity.Entity
import kotlinx.coroutines.launch

sealed class Command<T : Command<T>> (
    open val name: String,
    private val kord: Kord
) {
    internal lateinit var command: Entity
    val commandId get() = command.id

    abstract suspend fun init()

    final fun register() = kord.launch {
        init()
    }
}