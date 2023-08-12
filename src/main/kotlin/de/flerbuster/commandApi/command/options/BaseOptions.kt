package de.flerbuster.commandApi.command.options

import dev.kord.core.entity.*
import dev.kord.core.entity.channel.ResolvedChannel
import kotlinx.serialization.StringFormat
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json

sealed class BaseOptions {
    abstract val strings: Map<String, String>

    abstract val integers: Map<String, Long>

    abstract val numbers: Map<String, Double>

    abstract val booleans: Map<String, Boolean>

    abstract val users: Map<String, User>

    abstract val members: Map<String, Member>

    abstract val channels: Map<String, ResolvedChannel>

    abstract val roles: Map<String, Role>

    abstract val mentionables: Map<String, Entity>

    abstract val attachments: Map<String, Attachment>

    inline fun <reified T> custom(stringFormat: StringFormat = Json): HashMap<String, T> {
        val withTypeT = hashMapOf<String, T>()

        strings.forEach { (key, value) ->
            try {
                withTypeT += key to stringFormat.decodeFromString(value)
            } catch (_: Exception) {
            }
        }

        return withTypeT
    }

    operator fun <T : Comparable<T>> get(at: String): T? {
        return (strings[at] ?: integers[at] ?: numbers[at] ?: booleans[at] ?: users[at] ?: members[at] ?: channels[at] ?: roles[at] ?: mentionables[at] ?: attachments[at]) as? T
    }

    @JvmName("get2")
    operator fun get(at: String): Comparable<*>? {
        return strings[at] ?: integers[at] ?: numbers[at] ?: booleans[at] ?: users[at] ?: members[at] ?: channels[at] ?: roles[at] ?: mentionables[at] ?: attachments[at]
    }
}