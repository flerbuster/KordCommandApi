package de.flerbuster.commandApi.command.arguments.argument

import de.flerbuster.commandApi.command.arguments.type.ArgumentType
import dev.kord.rest.builder.interaction.BaseChoiceBuilder

data class Argument<T>(
    private val initialName: String,
    val builder: BaseChoiceBuilder<T>.() -> Unit,
    private val initialDescription: String
) {
    var type = ArgumentType.None
    val name = initialName.lowercase()
    val description = initialDescription.lowercase()
}