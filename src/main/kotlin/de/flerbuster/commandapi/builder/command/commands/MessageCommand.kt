package de.flerbuster.commandapi.builder.command.commands

import de.flerbuster.commandapi.builder.command.arguments.argument.Argument
import de.flerbuster.commandapi.builder.command.options.Options
import dev.kord.core.Kord
import dev.kord.core.entity.interaction.GlobalApplicationCommandInteraction
import dev.kord.core.event.interaction.GlobalChatInputCommandInteractionCreateEvent
import dev.kord.core.on

class MessageCommand(
    private val name: String,
    private val description: String,
    val execution: suspend (interaction: GlobalApplicationCommandInteraction, options: Options) -> Unit,
    val onInit: suspend (command: MessageCommand) -> Unit,
    private val arguments: MutableList<Argument>,
    private val kord: Kord
) : Command<MessageCommand>(name, kord) {
    override suspend fun init() {
        onInit(this)

        val command = kord.createGlobalMessageCommand(name)

        kord.on<GlobalChatInputCommandInteractionCreateEvent>(kord) {
            if (interaction.invokedCommandId == command.id) {
                execution(interaction, Options(interaction.command))
            }
        }
    }
}