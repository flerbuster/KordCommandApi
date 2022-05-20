package commandApi.builder.command.commands

import commandApi.builder.command.arguments.argument.Argument
import commandApi.builder.command.arguments.type.ArgTypes
import commandApi.builder.command.options.Options
import dev.kord.core.Kord
import dev.kord.core.entity.interaction.GlobalApplicationCommandInteraction
import dev.kord.core.entity.interaction.GlobalChatInputCommandInteraction
import dev.kord.core.entity.interaction.GuildChatInputCommandInteraction
import dev.kord.core.event.interaction.*
import dev.kord.core.kordLogger
import dev.kord.core.on
import dev.kord.rest.builder.interaction.*

class SlashCommand(
    val name: String,
    private val description: String,
    private val arguments: MutableList<Argument>,
    private val execution: suspend (interaction: GuildChatInputCommandInteraction, options: Options) -> Unit,
    private val onInit: suspend (command: SlashCommand) -> Unit,
    private val kord: Kord
) : Command<SlashCommand>(name, kord) {
    override suspend fun init() {
        onInit(this)

        val command = kord.createGlobalChatInputCommand(name, description) {
            arguments.forEach {
                when (it.type) {
                    ArgTypes.String -> string(it.name, it.description) { required = it.required }
                    ArgTypes.Boolean -> boolean(it.name, it.description) { required = it.required }
                    ArgTypes.User -> user(it.name, it.description) { required = it.required }
                    ArgTypes.Mentionable -> mentionable(it.name, it.description) { required = it.required }
                    ArgTypes.Channel -> channel(it.name, it.description) { required = it.required }
                    ArgTypes.Role -> role(it.name, it.description) { required = it.required }
                    ArgTypes.SubCommand -> subCommand(it.name, it.description) { required = it.required }
                    ArgTypes.Group -> group(it.name, it.description) { required = it.required }
                    ArgTypes.Int -> int(it.name, it.description) { required = it.required }
                    ArgTypes.None -> {  }
                }
            }
        }

        kord.on<ChatInputCommandInteractionCreateEvent>(kord) {
            try {
                if (interaction.command.rootId == command.id) {
                    execution(interaction as GuildChatInputCommandInteraction, Options(interaction.command))
                }
            } catch (e: Exception) {
                println("error $e at $name")
            }
        }
    }
}