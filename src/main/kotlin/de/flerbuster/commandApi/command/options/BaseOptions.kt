package de.flerbuster.commandApi.command.options

import dev.kord.core.entity.*
import dev.kord.core.entity.channel.ResolvedChannel
import kotlinx.serialization.StringFormat
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json

/**
 * Base class for [SlashCommandOptions] and [MessageCommandOptions]
 **/
sealed class BaseOptions {
    /**
     * holding all string arguments
     */
    abstract val strings: Map<String, String>

    /**
     * holding all integer arguments
     */
    abstract val integers: Map<String, Long>

    /**
     * holding all number arguments
     */
    abstract val numbers: Map<String, Double>

    /**
     * holding all boolean arguments
     */
    abstract val booleans: Map<String, Boolean>

    /**
     * holding all user arguments
     */
    abstract val users: Map<String, User>

    /**
     * holding all member arguments
     */
    abstract val members: Map<String, Member>

    /**
     * holding all channel arguments
     */
    abstract val channels: Map<String, ResolvedChannel>

    /**
     * holding all role arguments
     */
    abstract val roles: Map<String, Role>

    /**
     * holding all mentionable arguments
     */
    abstract val mentionables: Map<String, Entity>

    /**
     * holding all attachment arguments
     */
    abstract val attachments: Map<String, Attachment>

    /**
    * holding all arguments
     */
    val allArguments get() = mutableMapOf<String, Comparable<*>>().apply {
        putAll(strings)
        putAll(integers)
        putAll(numbers)
        putAll(booleans)
        putAll(users)
        putAll(members)
        putAll(channels)
        putAll(roles)
        putAll(mentionables)
        putAll(attachments)
    }

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
        return (allArguments[at]) as? T
    }

    @JvmName("get2")
    operator fun get(at: String): Comparable<*>? {
        return allArguments[at]
    }

    fun has(key: String): Boolean {
        return allArguments.containsKey(key)
    }
}