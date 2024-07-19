package de.flerbuster.commandApi.command.arguments.provider

import de.flerbuster.commandApi.command.arguments.argument.Argument
import de.flerbuster.commandApi.command.options.BaseOptions
import kotlin.reflect.KProperty

class DefaultArgumentDelegate<T>(val options: BaseOptions, private val default: T, val argument: Argument<T>) {
    operator fun getValue(thisRef: Any?, property: KProperty<*>): T {
        return (options.allArguments[argument.name] as T?) ?: default
    }
}

class DefaultCustomArgumentDelegate<T>(val options: BaseOptions, private val default: T, val value: T?) {
    operator fun getValue(thisRef: Any?, property: KProperty<*>): T {
        return value ?: default
    }
}