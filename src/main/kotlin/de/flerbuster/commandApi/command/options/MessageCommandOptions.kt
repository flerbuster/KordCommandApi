package de.flerbuster.commandApi.command.options

import de.flerbuster.commandApi.command.arguments.argument.Argument
import de.flerbuster.commandApi.command.arguments.argument.ArgumentBuilder
import de.flerbuster.commandApi.command.arguments.argument.CustomArgumentBuilder
import de.flerbuster.commandApi.command.arguments.type.ArgumentType
import de.flerbuster.commandApi.command.commands.MessageCommand
import dev.kord.common.entity.Snowflake
import dev.kord.core.entity.*
import dev.kord.core.entity.channel.ResolvedChannel
import dev.kord.rest.builder.interaction.BaseChoiceBuilder
import dev.kord.rest.builder.interaction.StringChoiceBuilder
import io.ktor.util.reflect.*
import kotlinx.coroutines.runBlocking

/**
 * respective [BaseOptions] for [MessageCommand]s
 */
class MessageCommandOptions(
    val message: Message,
    val command: MessageCommand,
) : BaseOptions() {
    companion object {
        fun String.parseArguments(): List<String> {
            val arguments = mutableListOf<String>()
            var currentArg = StringBuilder()
            var insideQuotes = false

            for (char in this) {
                when (char) {
                    ' ' -> {
                        if (!insideQuotes) {
                            if (currentArg.isNotEmpty()) {
                                arguments.add(currentArg.toString())
                                currentArg = StringBuilder()
                            }
                        } else {
                            currentArg.append(char)
                        }
                    }
                    '"' -> insideQuotes = !insideQuotes
                    else -> currentArg.append(char)
                }
            }

            if (currentArg.isNotEmpty()) {
                arguments.add(currentArg.toString())
            }

            return arguments
        }
    }

    private val commandArguments = message.content.replace(command.commandName, "").parseArguments()

    private val parsedArguments: List<ParsedArgument<*>> = runBlocking { parseArgumentsByName() }

    private fun String.extractId(afterAngleBracket: String ): Long? {
        val regexPattern = """<$afterAngleBracket(\d+)>""".toRegex()
        val matchResult = regexPattern.find(this)
        return matchResult?.groupValues?.get(1)?.toLongOrNull()
    }

    private suspend fun <T> Argument<T>.parseArgument(argument: String): T {
        return when (type) {
            ArgumentType.String -> argument

            ArgumentType.Boolean -> argument.toBoolean()

            ArgumentType.User -> {
                val id = argument.toLongOrNull() ?: argument.extractId("@") ?: error("invalid argument for User: $argument")
                val user = command.kord.getUser(Snowflake(id)) ?: error("user not found")
                user
            }

            ArgumentType.Member -> {
                val guild = message.getGuildOrNull() ?: error("only works in guild")
                val id = argument.toLongOrNull() ?: argument.extractId("@") ?: error("invalid argument for User: $argument")
                val member = guild.getMemberOrNull(Snowflake(id)) ?: error("member not found")
                member
            }

            ArgumentType.Mentionable -> {
                val userId = argument.extractId("@")
                if (userId != null) {
                    val user = command.kord.getUser(Snowflake(userId))
                    user ?: error("user not found")
                } else {
                    val channelId = argument.extractId("#")
                    if (channelId != null) {
                        val channel = command.kord.getChannel(Snowflake(channelId))
                        channel ?: error("channel not found")
                    } else {
                        val roleId = argument.extractId("@&")
                        if (roleId != null) {
                            val guild = message.getGuild()
                            val role = guild.getRoleOrNull(Snowflake(roleId))
                            role ?: error("role not found")
                        } else {
                            error("invalid argument for Mentionable: $argument")
                        }
                    }
                }
            }

            ArgumentType.Channel -> {
                val id = argument.toLongOrNull() ?: argument.extractId("#") ?: error("invalid argument for Channel: $argument")
                val channel = command.kord.getChannel(Snowflake(id)) ?: error("channel not found")
                channel
            }

            ArgumentType.Role -> {
                val guild = message.getGuild()
                val id = argument.toLongOrNull() ?: argument.extractId("@&") ?: error("invalid argument for Role: $argument")
                val role = guild.getRoleOrNull(Snowflake(id)) ?: error("role not found")
                role
            }

            ArgumentType.SubCommand -> {
                error("what is this")
            }

            ArgumentType.Group -> {
                error("what is this")
            }

            ArgumentType.Int -> argument.toIntOrNull() ?: error("invalid argument for Int: $argument")

            ArgumentType.Number -> argument.toDoubleOrNull() ?: error("invalid argument for Int: $argument")

            ArgumentType.Attachment -> argument

            ArgumentType.None -> argument

            ArgumentType.Custom -> argument
        } as T
    }

    private suspend fun parseArgumentsByName(): List<ParsedArgument<*>> {
        val args = commandArguments
        val argValues = hashMapOf<Argument<*>, String>()
        for (argument in args) {
            val split = argument.split("=", limit = 2)
            if (split.size != 2) continue
            val argName = split[0]
            val argValue = split[1]
            command.arguments.forEach { arg ->
                if (arg.name == argName) {
                    argValues[arg] = argValue
                }
            }
        }

        val parsedArgs = argValues.map {
            createParsedArgument(it.key, it.value)
        }

        return parsedArgs
    }

    private suspend fun <T> createParsedArgument(arg: Argument<T>, argument: String): ParsedArgument<T> {
        return ParsedArgument(arg, arg.parseArgument(argument))
    }

    private data class ParsedArgument<T>(
        val argument: Argument<T>,
        val value: T?
    )

    override val strings: Map<String, String> get() = parsedArguments.filter { it.argument.type == ArgumentType.String }.associate {
        it.argument.name to (it.value as String)
    }

    override val integers: Map<String, Long> get() = parsedArguments.filter { it.argument.type == ArgumentType.Int }.associate {
        it.argument.name to (it.value as Long)
    }

    override val numbers: Map<String, Double> get() = parsedArguments.filter { it.argument.type == ArgumentType.Number }.associate {
        it.argument.name to (it.value as Double)
    }

    override val booleans: Map<String, Boolean> get() = parsedArguments.filter { it.argument.type == ArgumentType.Boolean }.associate {
        it.argument.name to (it.value as Boolean)
    }

    override val users: Map<String, User> get() = parsedArguments.filter { it.argument.type == ArgumentType.User }.associate {
        it.argument.name to (it.value as User)
    }

    override val members: Map<String, Member> get() = parsedArguments.filter { it.argument.type == ArgumentType.User }.associate {
        it.argument.name to (it.value as Member)
    }

    override val channels: Map<String, ResolvedChannel> get() = parsedArguments.filter { it.argument.type == ArgumentType.Channel }.associate {
        it.argument.name to (it.value as ResolvedChannel)
    }

    override val roles: Map<String, Role> get() = parsedArguments.filter { it.argument.type == ArgumentType.Role }.associate {
        it.argument.name to (it.value as Role)
    }

    override val mentionables: Map<String, Entity> get() = parsedArguments.filter { it.argument.type == ArgumentType.Mentionable }.associate {
        it.argument.name to (it.value as Entity)
    }

    override val attachments: Map<String, Attachment> get() = parsedArguments.filter { it.argument.type == ArgumentType.Attachment }.associate {
        it.argument.name to (it.value as Attachment)
    }

    override fun toString(): String {
        return "MessageCommandOptions(" +
                "command?=$command?, " +
                "strings=$strings, " +
                "integers=$integers, " +
                "numbers=$numbers, " +
                "booleans=$booleans, " +
                "users=$users, " +
                "members=$members, " +
                "channels=$channels, " +
                "roles=$roles, " +
                "mentionables=$mentionables, " +
                "attachments=$attachments" +
                ")"
    }
}