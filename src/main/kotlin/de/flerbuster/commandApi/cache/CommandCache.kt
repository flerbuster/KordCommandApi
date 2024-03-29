package de.flerbuster.commandApi.cache

import de.flerbuster.commandApi.command.arguments.argument.Argument
import de.flerbuster.commandApi.command.arguments.type.ArgumentType
import de.flerbuster.commandApi.command.commands.Command
import de.flerbuster.commandApi.command.commands.SlashCommand
import dev.kord.common.entity.Snowflake
import dev.kord.core.Kord
import kotlinx.serialization.Serializable
import kotlinx.serialization.builtins.MapSerializer
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import java.io.File
import java.util.concurrent.ConcurrentHashMap

object CommandCache {
    val Kord.token get() = this.resources.token

    @Serializable
    internal data class CachedArgument(
        val name: String,
        val description: String,
        val type: ArgumentType,
    ) {
        companion object {
            fun fromArgument(argument: Argument<*>): CachedArgument {
                return CachedArgument(
                    argument.name,
                    argument.description,
                    argument.type
                )
            }
        }
    }

    @Serializable
    internal data class CachedCommand(
        val name: String,
        val description: String,
        val arguments: List<CachedArgument>,
        val id: Snowflake
    ) {
        companion object {
            fun fromSlashCommand(command: SlashCommand): CachedCommand {
                return CachedCommand(
                    command.name,
                    command.description,
                    command.arguments.map(CachedArgument::fromArgument),
                    command.command.id
                )
            }
        }
    }

    private val cacheDir by lazy {
        File(System.getProperty("user.home"), ".cache${File.separator}KordCommandApi").apply {
            if (!exists()) mkdirs()
        }
    }

    private val cacheFile by lazy {
        File(cacheDir, "commandcache.json").apply {
            if (!exists()) createNewFile()
        }
    }

    private val cache: MutableMap<String, MutableSet<CachedCommand>> by lazy { ConcurrentHashMap() }
    internal var enabled: Boolean = true

    init {
        loadCacheFromFile()
    }

    private fun loadCacheFromFile() {
        try {
            val json = cacheFile.readText()
            val newCache = Json.decodeFromString<MutableMap<String, MutableSet<CachedCommand>>>(json)
            cache.putAll(newCache)
        } catch (e: Exception) {
            println("Error loading cache: ${e.message}")
            e.printStackTrace()
        }
    }

    private fun saveCacheToFile() {
        try {
            val json = Json.encodeToString(cache)
            cacheFile.writeText(json)
        } catch (e: Exception) {
            println("Error saving cache: ${e.message}")
            e.printStackTrace()
        }
    }

    internal fun getCachedCommand(command: SlashCommand, kord: Kord = command.kord): CachedCommand? {
        return cache[kord.token]?.find { cachedCmd ->
            cachedCmd.name == command.name &&
                    cachedCmd.arguments == command.arguments.map { CachedArgument.fromArgument(it) } &&
                    cachedCmd.description == command.description
        }
    }

    internal fun cacheCommand(command: SlashCommand) {
        val cachedCommand = CachedCommand.fromSlashCommand(command)
        cache.compute(command.kord.token) { _, current ->
            (current ?: mutableSetOf()).apply { add(cachedCommand) }
        }
        saveCacheToFile()
    }

    fun clearCache() {
        cache.clear()
        saveCacheToFile()
    }

    fun enable() {
        enabled = true
    }

    fun disable() {
        enabled = false
    }
}