package de.flerbuster.commandApi.command.arguments.provider

import de.flerbuster.commandApi.command.arguments.argument.Argument
import de.flerbuster.commandApi.command.arguments.argument.CustomArgument
import de.flerbuster.commandApi.command.options.BaseOptions
import kotlin.reflect.KProperty

class ArgumentProvider(val options: BaseOptions) {
    inline fun <reified T> Any?.to(): T {
        if (Int is T) return this?.toString()?.toInt() as T
        if (Float is T) return this?.toString()?.toFloat() as T

        return this as T
    }

    inline fun <reified T> Argument<T>.get(): T {
        return options.allArguments[name].to()
    }

    inline fun <reified T> CustomArgument<T>.getCustom(): T {
        return options.custom<T>(format)[name] as T
    }

    inline fun <reified T> Argument<T?>.or(other: T): DefaultArgumentDelegate<T> {
        return DefaultArgumentDelegate(options, other, this) as DefaultArgumentDelegate<T>
    }

    inline fun <reified T> CustomArgument<T?>.or(other: T): DefaultCustomArgumentDelegate<T> {
        return DefaultCustomArgumentDelegate(options, other, getCustom<T?>())
    }

    inline operator fun <reified T> Argument<T>.getValue(thisRef: Any?, property: KProperty<*>): T {
        return get()
    }

    inline operator fun <reified T> CustomArgument<T>.getValue(thisRef: Any?, property: KProperty<*>): T {
        return getCustom()
    }
}