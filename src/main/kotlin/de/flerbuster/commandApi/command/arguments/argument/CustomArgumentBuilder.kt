package de.flerbuster.commandApi.command.arguments.argument

import dev.kord.common.annotation.KordDsl
import dev.kord.common.entity.ApplicationCommandOption
import dev.kord.common.entity.ApplicationCommandOptionType
import dev.kord.common.entity.Choice
import dev.kord.common.entity.optional.Optional
import dev.kord.common.entity.optional.OptionalBoolean
import dev.kord.common.entity.optional.delegate.delegate
import dev.kord.rest.builder.RequestBuilder
import dev.kord.rest.builder.interaction.BaseChoiceBuilder
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

/** das ist von kord geklaut
 *  [dev.kord.rest.builder.interaction.OptionsBuilder]
 **/

@KordDsl
class CustomArgumentBuilder(
    val name: String,
    val description: String
) : RequestBuilder<ApplicationCommandOption> {
    private var _default: OptionalBoolean = OptionalBoolean.Missing
    var default: Boolean? by ::_default.delegate()

    private var _required: OptionalBoolean = OptionalBoolean.Missing
    var required: Boolean? by ::_required.delegate()

    private var _autocomplete: OptionalBoolean = OptionalBoolean.Missing

    /**
     * Setting this to `true` allows you to dynamically respond with your choices, depending on the user input.
     *
     * This disables all input validation, users can submit values before responding to the AutoComplete request.
     *
     * Enabling this also means that you cannot add any other option.
     */
    var autocomplete: Boolean? by ::_autocomplete.delegate()

    private var _choices: Optional<MutableList<Choice<String>>> = Optional.Missing()
    var choices: MutableList<Choice<String>>? by ::_choices.delegate()

    inline fun <reified T> choice(name: String, value: T, json: Json = Json) {
        if (choices == null) choices = mutableListOf()
        (choices ?: return).add(Choice.StringChoice(name, json.encodeToString(value)))
    }

    fun toBaseChoiceBuilder(): BaseChoiceBuilder<String>.() -> Unit = {
        default = this@CustomArgumentBuilder.default
        required = this@CustomArgumentBuilder.required
        autocomplete = this@CustomArgumentBuilder.autocomplete

        this@CustomArgumentBuilder.choices?.forEach {
            choice(it.name, it.value)
        }
    }

    override fun toRequest(): ApplicationCommandOption = ApplicationCommandOption(
        ApplicationCommandOptionType.String,
        name,
        description,
        _default,
        _required,
        autocomplete = _autocomplete
    )
}