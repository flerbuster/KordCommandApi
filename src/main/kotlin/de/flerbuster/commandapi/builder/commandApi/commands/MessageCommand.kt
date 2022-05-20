package commandApi.builder.command.commands

import commandApi.builder.command.arguments.argument.Argument
import commandApi.builder.command.options.Options
import dev.kord.core.Kord
import dev.kord.core.entity.interaction.GuildApplicationCommandInteraction
import dev.kord.core.event.interaction.GuildMessageCommandInteractionCreateEvent
import dev.kord.core.on

class MessageCommand(
    private val name: String,
    private val description: String,
    val execution: suspend (interaction: GuildApplicationCommandInteraction, options: Options) -> Unit,
    val onInit: suspend (command: MessageCommand) -> Unit,
    private val arguments: MutableList<Argument>,
    private val kord: Kord
) : Command<MessageCommand>(name, kord) {
    override suspend fun init() {
        onInit(this)

        val command = kord.createGlobalMessageCommand(name)

        kord.on<GuildMessageCommandInteractionCreateEvent>(kord) {
            if (interaction.invokedCommandId == command.id) {
                execution(interaction, Options(null))
            }
        }
    }
}