package commandApi.builder.command.builders

import commandApi.builder.command.arguments.argument.Argument
import commandApi.builder.command.arguments.argument.ArgumentBuilder
import commandApi.builder.command.arguments.type.ArgTypes
import commandApi.builder.command.commands.Command
import commandApi.builder.command.options.Options
import commandApi.builder.errors.TypeNotSupportedError
import dev.kord.core.Kord
import dev.kord.core.entity.interaction.ApplicationCommandInteraction
import dev.kord.core.entity.interaction.GlobalApplicationCommandInteraction
import dev.kord.core.entity.interaction.GuildApplicationCommandInteraction

sealed class CommandBuilder<T : Command<*>>(
    open val name: String,
    open val description: String,
    open val kord: Kord
) {
    val arguments: MutableList<Argument> = mutableListOf()
    var defaultRequired = true
    internal var execution: suspend (interaction: GuildApplicationCommandInteraction, options: Options) -> Unit = { _, _ -> }
    internal var onInit: suspend T.() -> Unit = {  }

    fun runs(block: suspend (ApplicationCommandInteraction, Options) -> Unit) {
        execution = block
    }

    fun onInit(block: suspend T.() -> Unit = {  }) {
        onInit = block
    }

    inline fun <reified T> getType(): ArgTypes {
        return ArgTypes.from<T>()
            ?: throw TypeNotSupportedError("${T::class.qualifiedName} is not supported in discord arguments")
    }

    inline fun <reified T> argument(
        name: String,
        description: String,
        type: ArgTypes = getType<T>(),
        builder: ArgumentBuilder.() -> Unit = { }
    ): Argument {
        val argument = ArgumentBuilder(type, defaultRequired).apply(builder).build(name, description)
        arguments += argument
        return argument
    }

    abstract fun build(): T
}

