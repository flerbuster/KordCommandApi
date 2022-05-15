package de.flerbuster.commandapi.builder.command.arguments.type

import dev.kord.common.entity.*
import dev.kord.core.entity.interaction.GroupCommand
import kotlin.reflect.KClass

enum class ArgTypes(
    vararg val types: KClass<*>
) {
    String(
        kotlin.String::class
    ),
    Boolean(
        kotlin.Boolean::class
    ),
    User(
        dev.kord.core.entity.User::class,
        ApplicationCommandOptionType.User::class,
        DiscordGuildApplicationCommandPermission.Type.User::class,
        ApplicationCommandType.User::class
    ),
    Mentionable(
        ApplicationCommandOptionType.Mentionable::class,
        AuditLogChangeKey.Mentionable::class
    ),
    Channel(
        dev.kord.core.entity.channel.Channel::class,
        ApplicationCommandOptionType.Channel::class
    ),
    Role(
        dev.kord.core.entity.Role::class,
        ApplicationCommandOptionType.Role::class,
        OverwriteType.Role::class,
        DiscordGuildApplicationCommandPermission.Type.Role::class,
    ),
    SubCommand(
        dev.kord.common.entity.SubCommand::class,
        ApplicationCommandOptionType.SubCommand::class,
        dev.kord.core.entity.interaction.SubCommand::class,
    ),
    Group(
        GroupCommand::class
    ),
    Int(
        kotlin.Int::class,
    ),
    None(
        Nothing::class
    );

    companion object {
        inline fun <reified T> from(
            type: KClass<*> = T::class
        ): ArgTypes? = values().firstOrNull { type in it.types }
    }

}