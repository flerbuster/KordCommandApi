package de.flerbuster.commandApi.command.commands

import de.flerbuster.commandApi.cache.CommandCache
import de.flerbuster.commandApi.command.arguments.argument.Argument
import de.flerbuster.commandApi.command.arguments.type.ArgumentType
import de.flerbuster.commandApi.command.options.SlashCommandOptions
import dev.kord.common.entity.Snowflake
import dev.kord.core.Kord
import dev.kord.core.entity.Entity
import dev.kord.core.entity.interaction.ChatInputCommandInteraction
import dev.kord.core.event.interaction.*
import dev.kord.core.on
import dev.kord.rest.builder.interaction.*
import kotlin.reflect.KClass

class SlashCommand(
    override val name: String,
    override val description: String,
    override val arguments: MutableList<Argument<*>>,
    private val execution: suspend (interaction: ChatInputCommandInteraction, options: SlashCommandOptions) -> Unit,
    private val exceptionHandling: HashMap<KClass<Exception>, suspend (
        exception: Exception,
        interaction: ChatInputCommandInteraction,
        command: SlashCommand
    ) -> Unit>,
    val kord: Kord,
    private val recache: Boolean
) : Command<SlashCommand>(name, description, arguments, kord) {
    override val commandName: String get() = "/$name"

    /**
     * initializes the command, listening for interactions being made with the command id
     */
    @Suppress("UNCHECKED_CAST")
    override suspend fun init() {
        println("creating command $name")

        val cachedCommand = CommandCache.getCachedCommand(this, kord)
        if (cachedCommand != null && CommandCache.enabled && !recache) command = object : Entity {
            override val id: Snowflake = cachedCommand.id
        }
        else {
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

                            ArgumentType.Member -> user(
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

                            ArgumentType.Int -> integer(
                                it.name,
                                it.description,
                                it.builder as IntegerOptionBuilder.() -> Unit
                            )

                            ArgumentType.Number -> number(
                                it.name,
                                it.description,
                                it.builder as NumberOptionBuilder.() -> Unit
                            )

                            ArgumentType.Attachment -> attachment(
                                it.name,
                                it.description,
                                it.builder as AttachmentBuilder.() -> Unit
                            )

                            else -> { }
                        }
                    } catch (e: Exception) {
                        println(
                            "got an unexpected error during the creation of the '${it.name}' argument (${name} command), " +
                                    "this could be because the builder for ${it.type} is no 'BaseChoiceBuilder' " +
                                    "try calling the 'basicArgument' function instead of the 'argument' function"
                        )
                        e.printStackTrace()
                    }
                }
            }
            if (CommandCache.enabled) CommandCache.cacheCommand(this)
        }


        kord.on<ChatInputCommandInteractionCreateEvent>(kord) {
            try {
                if (interaction.command.rootId == command.id) {
                    execution(interaction, SlashCommandOptions(interaction.command))
                }
            } catch (e: Exception) {
                exceptionHandling.forEach { (type, handler) ->
                    if (type.isInstance(e)) {
                        handler(
                            e,
                            interaction,
                            this@SlashCommand
                        )
                    }
                }
            }
        }
    }
}