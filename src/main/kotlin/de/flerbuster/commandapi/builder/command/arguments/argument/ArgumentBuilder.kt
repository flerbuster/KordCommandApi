package de.flerbuster.commandapi.builder.command.arguments.argument

import de.flerbuster.commandapi.builder.command.arguments.type.ArgTypes

class ArgumentBuilder(
    private var type: ArgTypes,
    defaultRequired: Boolean
) {
    var required: Boolean = defaultRequired

    fun build(
        name: String,
        description: String
    ): Argument {
        return Argument(name, required, type, description)
    }
}