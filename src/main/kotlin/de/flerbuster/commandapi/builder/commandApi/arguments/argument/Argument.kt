package commandApi.builder.command.arguments.argument

import commandApi.builder.command.arguments.type.ArgTypes

data class Argument(
    private val initialName: String,
    val required: Boolean,
    val type: ArgTypes,
    private val initialDescription: String
) {
    val name = initialName.lowercase()
    val description = initialDescription.lowercase()
}