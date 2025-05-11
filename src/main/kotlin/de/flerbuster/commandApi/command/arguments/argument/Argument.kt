package de.flerbuster.commandApi.command.arguments.argument

import de.flerbuster.commandApi.command.arguments.type.ArgumentType
import dev.kord.rest.builder.interaction.BaseChoiceBuilder

/**
 * a class for command Arguments.
 */
open class Argument<T>(
    internal open val initialName: String,
    open val builder: BaseChoiceBuilder<T, *>.() -> Unit,
    internal open val initialDescription: String
) {
    var type = ArgumentType.None
    val name get() = initialName.lowercase()
    val description get() = initialDescription.lowercase()

    override fun toString(): String {
        return "Argument(initialName='$initialName', builder=$builder, initialDescription='$initialDescription', type=$type, name='$name', description='$description')"
    }

}