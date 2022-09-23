package de.flerbuster.commandApi.command.commands

import de.flerbuster.commandApi.command.arguments.argument.Argument
import de.flerbuster.commandApi.command.options.Options
import dev.kord.core.Kord
import dev.kord.core.entity.interaction.GuildMessageCommandInteraction
import dev.kord.core.event.interaction.GuildMessageCommandInteractionCreateEvent
import dev.kord.core.on

class MessageCommand(
    val name: String,
    val description: String,
    private val execution: suspend (interaction: GuildMessageCommandInteraction, options: Options) -> Unit,
    private val arguments: MutableList<Argument<*>>,
    private val kord: Kord
) : Command<MessageCommand>(name, kord) {
    // kann das nicht
    override suspend fun init() {
        command = kord.createGlobalMessageCommand(name)

        kord.on<GuildMessageCommandInteractionCreateEvent>(kord) {
            if (interaction.invokedCommandId == command.id) {
                execution(interaction, Options(null))
            }
        }
    }
}
