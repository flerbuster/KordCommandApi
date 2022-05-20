package commandApi.builder.command.arguments.arguments

import commandApi.builder.command.commands.Command
import commandApi.builder.command.arguments.argument.Argument
import commandApi.builder.command.arguments.argument.ArgumentBuilder
import commandApi.builder.command.arguments.type.ArgTypes
import commandApi.builder.errors.TypeNotSupportedError

@Deprecated(
    "arguments are supported in CommandBuilder",
    ReplaceWith(
        "SlashCommandBuilder<C>",
        "commandApi.builder.command.builders.SlashCommandBuilder"
    ),
    DeprecationLevel.ERROR
)
@Suppress("UNUSED")
class ArgumentsBuilder<C : Command<C>> {
    val arguments = mutableListOf<Argument>()
    var defaultRequired: Boolean = true

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

    fun build() = Arguments(arguments)
}