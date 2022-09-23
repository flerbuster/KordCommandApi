package de.flerbuster.commandApi.command.options

import dev.kord.core.entity.*
import dev.kord.core.entity.channel.ResolvedChannel
import dev.kord.core.entity.interaction.InteractionCommand

class Options(
    val command: InteractionCommand?
) {
    val strings: Map<String, String> get() = command?.strings ?: mapOf()

    val integers: Map<String, Long> get() = command?.integers ?: mapOf()

    val numbers: Map<String, Double> get() = command?.numbers ?: mapOf()

    val booleans: Map<String, Boolean> get() = command?.booleans ?: mapOf()

    val users: Map<String, User> get() = command?.users ?: mapOf()

    val members: Map<String, Member> get() = command?.members ?: mapOf()

    val channels: Map<String, ResolvedChannel> get() = command?.channels ?: mapOf()

    val roles: Map<String, Role> get() = command?.roles ?: mapOf()

    val mentionables: Map<String, Entity> get() = command?.mentionables ?: mapOf()

    val attachments: Map<String, Attachment> get() = command?.attachments ?: mapOf()

    @Suppress("UNCHECKED_CAST")
    operator fun <T : Comparable<T>> get(at: String): T? {
        return (strings[at] ?: integers[at] ?: numbers[at] ?: booleans[at] ?: users[at] ?: members[at] ?: channels[at]
        ?: roles[at] ?: mentionables[at] ?: attachments[at]) as? T
    }

    @JvmName("get2")
    operator fun get(at: String): Comparable<*>? {
        return strings[at] ?: integers[at] ?: numbers[at] ?: booleans[at] ?: users[at] ?: members[at] ?: channels[at]
        ?: roles[at] ?: mentionables[at] ?: attachments[at]
    }

    fun isEmpty(): Boolean {
        return command?.options?.isEmpty() ?: true
    }

    override fun toString(): String {
        return "Options(" +
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