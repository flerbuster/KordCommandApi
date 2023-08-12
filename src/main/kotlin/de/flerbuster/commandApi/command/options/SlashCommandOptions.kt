package de.flerbuster.commandApi.command.options

import dev.kord.core.entity.*
import dev.kord.core.entity.channel.ResolvedChannel
import dev.kord.core.entity.interaction.InteractionCommand
import kotlinx.serialization.StringFormat
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json

class SlashCommandOptions(
    val command: InteractionCommand?
) : BaseOptions() {
    override val strings: Map<String, String> get() = command?.strings ?: mapOf()

    override val integers: Map<String, Long> get() = command?.integers ?: mapOf()

    override val numbers: Map<String, Double> get() = command?.numbers ?: mapOf()

    override val booleans: Map<String, Boolean> get() = command?.booleans ?: mapOf()

    override val users: Map<String, User> get() = command?.users ?: mapOf()

    override val members: Map<String, Member> get() = command?.members ?: mapOf()

    override val channels: Map<String, ResolvedChannel> get() = command?.channels ?: mapOf()

    override val roles: Map<String, Role> get() = command?.roles ?: mapOf()

    override val mentionables: Map<String, Entity> get() = command?.mentionables ?: mapOf()

    override val attachments: Map<String, Attachment> get() = command?.attachments ?: mapOf()

    fun isEmpty(): Boolean {
        return command?.options?.isEmpty() ?: true
    }

    override fun toString(): String {
        return "SlashCommandOptions(" +
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