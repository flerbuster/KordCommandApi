package de.flerbuster.commandapi.builder.command.options

import dev.kord.core.entity.*
import dev.kord.core.entity.channel.ResolvedChannel
import dev.kord.core.entity.interaction.GuildChatInputCommandInteraction
import dev.kord.core.entity.interaction.InteractionCommand

class Options(val command: InteractionCommand) {
    val strings: Map<String, String> get() = command.strings

    val integers: Map<String, Long> get() = command.integers

    val numbers: Map<String, Double> get() = command.numbers

    val booleans: Map<String, Boolean> get() = command.booleans

    val users: Map<String, User> get() = command.users

    val members: Map<String, Member> get() = command.members

    val channels: Map<String, ResolvedChannel> get() = command.channels

    val roles: Map<String, Role> get() = command.roles

    val mentionables: Map<String, Entity> get() = command.mentionables

    val attachments: Map<String, Attachment> get() = command.attachments

    fun isEmpty(): Boolean {
        return command.options.isEmpty()
    }

    override fun toString(): String {
        return "Options(" +
                "command=$command, " +
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