package de.flerbuster.commandApi.command.arguments.argument

import dev.kord.rest.builder.interaction.OptionsBuilder

/**
 * class for building arguments
 */
class ArgumentBuilder<T> {
    var baseChoiceBuilder: OptionsBuilder.() -> Unit = { }

    /**
     * builds the argument
     */
    fun build(
        name: String,
        description: String
    ): Argument<T> {
        return Argument(name, baseChoiceBuilder, description)
    }
}