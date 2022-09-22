package de.flerbuster.commandApi.builder.command.commands

import dev.kord.core.Kord
import dev.kord.core.entity.Entity
import kotlinx.coroutines.launch

sealed class Command<T : Command<T>> (
    private val name: String,
    private val kord: Kord
) {
    internal lateinit var command: Entity

    abstract suspend fun init()

    final fun register() = kord.launch {
        init()
    }
}