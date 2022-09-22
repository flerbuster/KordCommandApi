package de.flerbuster.commandApi.builder.command.commands

import de.flerbuster.commandApi.builder.command.arguments.argument.Argument
import de.flerbuster.commandApi.builder.command.options.Options
import dev.kord.core.Kord
import dev.kord.core.entity.interaction.GlobalMessageCommandInteraction
import dev.kord.core.event.interaction.GlobalMessageCommandInteractionCreateEvent
import dev.kord.core.on

class MessageCommand(
    val name: String,
    val description: String,
    private val execution: suspend (interaction: GlobalMessageCommandInteraction, options: Options) -> Unit,
    private val arguments: MutableList<Argument<*>>,
    private val kord: Kord
) : Command<MessageCommand>(name, kord) {
    // kann das nicht
    override suspend fun init() {
        command = kord.createGlobalMessageCommand(name)

        kord.on<GlobalMessageCommandInteractionCreateEvent>(kord) {
            if (interaction.invokedCommandId == command.id) {
                execution(interaction, Options(null))
            }
        }
    }
}
