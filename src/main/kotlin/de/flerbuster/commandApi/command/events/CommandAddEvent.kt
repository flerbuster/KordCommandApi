package de.flerbuster.commandApi.command.events

import de.flerbuster.commandApi.command.commands.Command
import dev.kord.core.event.Event

interface CommandAddEvent<T : Command<T>>: Event {
    val command: T
}