package de.flerbuster.commandApi.builder.command.arguments.argument

import dev.kord.rest.builder.interaction.OptionsBuilder

class ArgumentBuilder<T> {
    var baseChoiceBuilder: OptionsBuilder.() -> Unit = {
    }

    fun build(
        name: String,
        description: String
    ): Argument<T> {
        return Argument(name, baseChoiceBuilder, description)
    }
}