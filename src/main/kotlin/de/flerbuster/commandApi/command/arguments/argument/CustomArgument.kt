package de.flerbuster.commandApi.command.arguments.argument

import dev.kord.rest.builder.interaction.BaseChoiceBuilder
import kotlinx.serialization.StringFormat


data class CustomArgument<T>(
    val format: StringFormat,
    override val initialName: String,
    override val builder: BaseChoiceBuilder<String>.() -> Unit,
    override val initialDescription: String
) : Argument<String>(initialName, builder, initialDescription)

