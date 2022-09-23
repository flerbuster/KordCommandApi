package de.flerbuster.commandApi.command.commands

import de.flerbuster.commandApi.command.arguments.argument.Argument
import de.flerbuster.commandApi.command.arguments.type.ArgumentType
import de.flerbuster.commandApi.command.options.Options
import dev.kord.core.Kord
import dev.kord.core.entity.interaction.GuildChatInputCommandInteraction
import dev.kord.core.event.interaction.*
import dev.kord.core.on
import dev.kord.rest.builder.interaction.*

class SlashCommand(
    val name: String,
    val description: String,
    private val arguments: MutableList<Argument<*>>,
    private val execution: suspend (interaction: GuildChatInputCommandInteraction, options: Options) -> Unit,
    private val kord: Kord
) : Command<SlashCommand>(name, kord) {
    @Suppress("UNCHECKED_CAST")
    override suspend fun init() {
        command = kord.createGlobalChatInputCommand(name, description) {
            arguments.forEach {
                try {
                    when (it.type) {
                        ArgumentType.String -> string(
                            it.name,
                            it.description,
                            it.builder as StringChoiceBuilder.() -> Unit
                        )
                        ArgumentType.Boolean -> boolean(
                            it.name,
                            it.description,
                            it.builder as BooleanBuilder.() -> Unit
                        )
                        ArgumentType.User -> user(
                            it.name,
                            it.description,
                            it.builder as UserBuilder.() -> Unit
                        )
                        ArgumentType.Mentionable -> mentionable(
                            it.name,
                            it.description,
                            it.builder as MentionableBuilder.() -> Unit
                        )
                        ArgumentType.Channel -> channel(
                            it.name,
                            it.description,
                            it.builder as ChannelBuilder.() -> Unit
                        )
                        ArgumentType.Role -> role(
                            it.name,
                            it.description,
                            it.builder as RoleBuilder.() -> Unit
                        )
                        ArgumentType.SubCommand -> subCommand(
                            it.name,
                            it.description,
                            it.builder as SubCommandBuilder.() -> Unit
                        )
                        ArgumentType.Group -> group(
                            it.name,
                            it.description,
                            it.builder as GroupCommandBuilder.() -> Unit
                        )
                        ArgumentType.Int -> int(
                            it.name,
                            it.description,
                            it.builder as IntChoiceBuilder.() -> Unit
                        )
                        ArgumentType.Number -> number(
                            it.name,
                            it.description,
                            it.builder as NumberChoiceBuilder.() -> Unit
                        )
                        ArgumentType.Attachment -> attachment(
                            it.name,
                            it.description,
                            it.builder as AttachmentBuilder.() -> Unit
                        )
                        ArgumentType.None -> {}
                    }
                } catch (e: Exception) {
                    println("got an unexpected error during the creation of the '${it.name}' argument (${name} command), " +
                            "this could be because the builder for ${it.type} is no 'BaseChoiceBuilder' " +
                            "try calling the 'basicArgument' function instead of the 'argument' function"
                    )
                    e.printStackTrace()
                }
            }
        }

        kord.on<ChatInputCommandInteractionCreateEvent>(kord) {
            try {
                if (interaction.command.rootId == command.id) {
                    execution(interaction as GuildChatInputCommandInteraction, Options(interaction.command))
                }
            } catch (e: Exception) {
                println("error '$e' at command '$name'")
                e.printStackTrace()
            }
        }
    }
}