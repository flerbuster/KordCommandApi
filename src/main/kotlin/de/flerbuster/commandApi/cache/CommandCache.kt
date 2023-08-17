package de.flerbuster.commandApi.cache

import de.flerbuster.commandApi.command.commands.Command
import dev.kord.common.entity.Snowflake
import kotlinx.serialization.Serializable
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import java.io.File
import java.util.concurrent.ConcurrentHashMap

object CommandCache {
    @Serializable
    data class CachedCommand(
        val commandName: String,
        val commandId: Snowflake
    )

    private val cacheDir = File(System.getProperty("user.home"), ".cache${File.separator}KordCommandApi")
    private val cacheFile = File(cacheDir, "commandcache.json")
    private val cache: MutableMap<String, CachedCommand> = ConcurrentHashMap()

    init {
        if (!cacheFile.parentFile.exists()) cacheFile.parentFile.mkdirs()
        if (!cacheFile.exists()) {
            cacheFile.createNewFile()
            cacheFile.writeText(Json.encodeToString(emptyList<CachedCommand>()))
        } else {
            loadCacheFromFile()
        }
    }

    private fun loadCacheFromFile() {
        try {
            val json = cacheFile.readText()
            val cachedCommands = Json.decodeFromString<List<CachedCommand>>(json)
            cachedCommands.forEach { cache[it.commandName] = it }
        } catch (e: Exception) {
            println(e.message + " loading cache")
            e.printStackTrace()
        }
    }

    private fun saveCacheToFile() {
        try {
            val json = Json.encodeToString(cache.values.toList())
            cacheFile.writeText(json)
        } catch (e: Exception) {
            println(e.message + " saving cache")
            e.printStackTrace()
        }
    }

    fun getCachedCommand(commandName: String): CachedCommand? {
        return cache[commandName]
    }

    fun cacheCommand(command: Command<*>) {
        val cachedCommand = CachedCommand(command.name, command.command.id)
        cache[command.name] = cachedCommand
        saveCacheToFile()
    }

    fun clearCache() {
        cache.clear()
        saveCacheToFile()
    }
}
