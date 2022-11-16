package de.flerbuster.commandApi.command.builders

import de.flerbuster.commandApi.command.arguments.argument.Argument
import de.flerbuster.commandApi.command.arguments.argument.ArgumentBuilder
import de.flerbuster.commandApi.command.arguments.argument.CustomArgumentBuilder
import de.flerbuster.commandApi.command.arguments.type.ArgumentType
import de.flerbuster.commandApi.command.commands.Command
import de.flerbuster.commandApi.command.options.Options
import de.flerbuster.commandApi.errors.TypeNotSupportedError
import dev.kord.core.Kord
import dev.kord.core.entity.interaction.ApplicationCommandInteraction
import dev.kord.core.entity.interaction.GuildApplicationCommandInteraction
import dev.kord.rest.builder.interaction.BaseChoiceBuilder
import dev.kord.rest.builder.interaction.OptionsBuilder
import kotlin.reflect.KClass

sealed class CommandBuilder<T : Command<*>>(
    open val name: String,
    open val description: String,
    open val kord: Kord
) {
    val arguments: MutableList<Argument<*>> = mutableListOf()
    var defaultRequired = false
    internal var execution: suspend (interaction: GuildApplicationCommandInteraction, options: Options) -> Unit =
        { _, _ -> }
    val exceptionHandlers: HashMap<KClass<Exception>, suspend (
        exception: Exception,
        interaction: GuildApplicationCommandInteraction,
        command: T
    ) -> Unit> = hashMapOf()

    fun runs(block: suspend (ApplicationCommandInteraction, Options) -> Unit) {
        execution = block
    }

     fun catches(
        exception: KClass<Exception>,
         block: suspend (
            exception: Exception,
            interaction: GuildApplicationCommandInteraction,
            command: T
        ) -> Unit
    ) {
        exceptionHandlers[exception] = block
    }

    inline fun <reified E : Exception> catches(
        noinline block: suspend (
            exception: Exception,
            interaction: GuildApplicationCommandInteraction,
            command: T
        ) -> Unit
    ) = catches(E::class as KClass<Exception>, block)

    @Suppress("UNCHECKED_CAST")
    inline fun <reified T> argument(
        name: String,
        description: String,
        noinline builder: BaseChoiceBuilder<T>.() -> Unit = { }
    ): Argument<T> {
        return addArgument(name, description, builder as OptionsBuilder.() -> Unit)
    }

    inline fun customArgument(
        name: String,
        description: String,
        noinline builder: CustomArgumentBuilder.() -> Unit = { }
    ): Argument<String> {
        return argument(
            name,
            description,
            CustomArgumentBuilder(name, description)
                .apply(builder).toBaseChoiceBuilder()
        )
    }

    inline fun <reified T> basicArgument(
        name: String,
        description: String,
        noinline builder: OptionsBuilder.() -> Unit = { }
    ): Argument<T> {
        return addArgument(name, description, builder)
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
            } }.build(name, description).apply {
                type = ArgumentType.from<T>()
                    ?: throw TypeNotSupportedError("'${T::class.qualifiedName}' is not supported in discord arguments")
            }
        arguments += argument
        return argument
    }

    internal abstract fun build(): T
}