package de.flerbuster.commandapi.builder.command.commands

import dev.kord.core.Kord
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

sealed class Command<T : Command<T>> (
    private val name: String,
    private val kord: Kord
) {
    private val scope: CoroutineScope = kord

    abstract suspend fun init()

    fun add() = scope.launch {
        init()
    }
}