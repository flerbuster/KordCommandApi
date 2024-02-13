package de.flerbuster.commandApi.command.builders

import de.flerbuster.commandApi.command.arguments.argument.Argument
import de.flerbuster.commandApi.command.arguments.argument.ArgumentBuilder
import de.flerbuster.commandApi.command.arguments.argument.CustomArgumentBuilder
import de.flerbuster.commandApi.command.arguments.type.ArgumentType
import de.flerbuster.commandApi.command.commands.Command
import de.flerbuster.commandApi.command.options.BaseOptions
import de.flerbuster.commandApi.errors.TypeNotSupportedError
import dev.kord.core.Kord
import dev.kord.core.entity.KordEntity
import dev.kord.rest.builder.interaction.BaseChoiceBuilder
import dev.kord.rest.builder.interaction.OptionsBuilder
import io.ktor.utils.io.*
import kotlin.reflect.KClass

/**
 * Base Builder class for [de.flerbuster.commandApi.command.commands.SlashCommand] and [de.flerbuster.commandApi.command.commands.MessageCommand]
 */
sealed class CommandBuilder<T : Command<*>, IC : KordEntity, OC : BaseOptions>(
    open val name: String,
    open val description: String,
    open val kord: Kord
) {
    /**
     * List of [Argument]s of the command.
     */
    val arguments: MutableList<Argument<*>> = mutableListOf()

    /**
     * if all arguments created are required by default.
     */
    var defaultRequired = false

    internal var execution: suspend (interaction: IC, options: OC) -> Unit =
        { _, _ -> }
    internal val exceptionHandlers: HashMap<KClass<Exception>, suspend (
        exception: Exception,
        interaction: IC,
        command: T
    ) -> Unit> =
        hashMapOf(Exception::class to { exception, _, command -> println("exception '${exception.message}' at command ${command.name}"); exception.printStack() })

    /**
     * Sets the execution of the command.
     */
    fun runs(block: suspend (IC, OC) -> Unit) {
        execution = block
    }

    /**
     * catches a specific exception and executes a block.
     */
    fun catches(
        exception: KClass<Exception>,
        block: suspend (
            exception: Exception,
            interaction: IC,
            command: T
        ) -> Unit
    ) {
        exceptionHandlers[exception] = block
    }

    /**
     * catches a specific exception and executes a block.
     */
    @Suppress("UNCHECKED_CAST")
    inline fun <reified E : Exception> catches(
        noinline block: suspend (
            exception: Exception,
            interaction: IC,
            command: T
        ) -> Unit
    ) = catches(E::class as KClass<Exception>, block)

    /**
     * Adds an argument to the command.
     *
     * @type [ArgumentType]
     */
    @Suppress("UNCHECKED_CAST")
    inline fun <reified T> argument(
        name: String,
        description: String,
        noinline builder: BaseChoiceBuilder<T>.() -> Unit = { }
    ): Argument<T> {
        return addArgument(name, description, builder as OptionsBuilder.() -> Unit)
    }

    inline fun <reified T> basicArgument(
        name: String,
        description: String,
        noinline builder: OptionsBuilder.() -> Unit = { }
    ): Argument<T> {
        return addArgument(name, description, builder)
    }

    fun customArgument(
        name: String,
        description: String,
        builder: CustomArgumentBuilder.() -> Unit = { }
    ): Argument<String> {
        return argument(
            name,
            description,
            CustomArgumentBuilder(name, description)
                .apply(builder).toBaseChoiceBuilder()
        )
    }

    inline fun <reified T> addArgument(
        name: String,
        description: String,
        noinline builder: OptionsBuilder.() -> Unit = { }
    ): Argument<T> {
        val argument = ArgumentBuilder<T>()
            .apply { baseChoiceBuilder = {
                required = defaultRequired
                builder()
            }
            }.build(name, description).apply {
                type = ArgumentType.from<T>()
                    ?: throw TypeNotSupportedError(T::class)
            }
        arguments += argument
        return argument
    }

    internal abstract fun build(): T
}