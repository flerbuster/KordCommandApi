package de.flerbuster.commandapi.builder.command.arguments.argument

import de.flerbuster.commandapi.builder.command.arguments.type.ArgTypes

data class Argument(
    private val initialName: String,
    val required: Boolean,
    val type: ArgTypes,
    private val initialDescription: String
) {
    val name = initialName.lowercase()
    val description = initialDescription.lowercase()
}