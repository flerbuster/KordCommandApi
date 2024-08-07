package de.flerbuster.commandApi.command.arguments.type

import dev.kord.common.entity.*
import dev.kord.core.entity.interaction.GroupCommand
import kotlin.reflect.KClass

/**
 * holding the different types of arguments
 */
enum class ArgumentType(
    vararg val types: KClass<*>
) {
    String(
        kotlin.String::class
    ),
    Boolean(
        kotlin.Boolean::class
    ),
    User(
        dev.kord.core.entity.User::class
    ),
    Member(
        dev.kord.core.entity.Member::class
    ),
    Mentionable(
        ApplicationCommandOptionType.Mentionable::class,
        AuditLogChangeKey.Mentionable::class
    ),
    Channel(
        dev.kord.core.entity.channel.Channel::class
    ),
    Role(
        dev.kord.core.entity.Role::class,
    ),
    SubCommand(
        dev.kord.common.entity.SubCommand::class,
    ),
    Group(
        GroupCommand::class
    ),
    Int(
        kotlin.Int::class,
        Long::class,
    ),
    Number(
        kotlin.Number::class,
        Double::class,
        Float::class
    ),
    Attachment(
        dev.kord.core.entity.Attachment::class,
        ApplicationCommandOptionType.Attachment::class
    ),
    Custom(

    ),
    None(
        Nothing::class
    );

    companion object {
        /**
         * finds the argument type from a class
         *
         * @param type the class to check
         */
        inline fun <reified T> from(
            type: KClass<*> = T::class
        ): ArgumentType? = entries.firstOrNull {
            type in it.types || it::class == type
        }
    }

}
