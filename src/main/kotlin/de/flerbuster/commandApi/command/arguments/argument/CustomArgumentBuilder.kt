package de.flerbuster.commandApi.command.arguments.argument

import dev.kord.common.Locale
import dev.kord.common.annotation.KordDsl
import dev.kord.common.entity.ApplicationCommandOption
import dev.kord.common.entity.ApplicationCommandOptionType
import dev.kord.common.entity.Choice
import dev.kord.common.entity.optional.Optional
import dev.kord.common.entity.optional.OptionalBoolean
import dev.kord.common.entity.optional.OptionalInt
import dev.kord.common.entity.optional.delegate.delegate
import dev.kord.rest.builder.RequestBuilder
import dev.kord.rest.builder.interaction.BaseChoiceBuilder
import dev.kord.rest.builder.interaction.ChoiceLocalizationsBuilder
import dev.kord.rest.builder.interaction.LocalizedDescriptionBuilder
import dev.kord.rest.builder.interaction.LocalizedNameBuilder
import kotlinx.serialization.StringFormat
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

/** das ist von kord geklaut
 *  [dev.kord.rest.builder.interaction.OptionsBuilder]
 *
 *  used for building string arguments, with custom serialized values
 **/
@KordDsl
class CustomArgumentBuilder<V>(
    override val name: String,
    override val description: String,
    val stringFormat: StringFormat = Json,
) : RequestBuilder<ApplicationCommandOption>, LocalizedNameBuilder, LocalizedDescriptionBuilder {
    /**
    *  from [dev.kord.rest.builder.interaction.OptionsBuilder]
     */
    internal var _default: OptionalBoolean = OptionalBoolean.Missing
    public var default: Boolean? by ::_default.delegate()
    internal var _nameLocalizations: Optional<MutableMap<Locale, String>?> = Optional.Missing()
    override var nameLocalizations: MutableMap<Locale, String>? by ::_nameLocalizations.delegate()
    internal var _descriptionLocalizations: Optional<MutableMap<Locale, String>?> = Optional.Missing()
    override var descriptionLocalizations: MutableMap<Locale, String>? by ::_descriptionLocalizations.delegate()

    internal var _required: OptionalBoolean = OptionalBoolean.Missing
    public var required: Boolean? by ::_required.delegate()

    internal var _autocomplete: OptionalBoolean = OptionalBoolean.Missing

    /**
     * Setting this to `true` allows you to dynamically respond with your choices, depending on the user input.
     *
     * This disables all input validation, users can submit values before responding to the AutoComplete request.
     *
     * Enabling this also means that you cannot add any other option.
     */
    public var autocomplete: Boolean? by ::_autocomplete.delegate()


    /**
     * from [dev.kord.rest.builder.interaction.BaseChoiceBuilder]
     */
    internal var _choices: Optional<MutableList<Choice.StringChoice>> = Optional.Missing()
    public var choices: MutableList<Choice.StringChoice>? by ::_choices.delegate()


    /**
     * from [dev.kord.rest.builder.interaction.StringChoiceBuilder]
     */
    private var _minLength: OptionalInt = OptionalInt.Missing

    /**
     * The minimum allowed string length (minimum of `0`, maximum of `6000`).
     */
    public var minLength: Int? by ::_minLength.delegate()

    private var _maxLength: OptionalInt = OptionalInt.Missing

    /**
     * The maximum allowed string length (minimum of `1`, maximum of `6000`).
     */
    public var maxLength: Int? by ::_maxLength.delegate()

    /**
     * adds a choice to the argument
     *
     * @param name the name of the choice
     * @param value the value of the choice
     * @param nameLocalizations the name localizations of the choice
     * @param stringFormat the format of the value
     */
    inline fun <reified T : V> choice(
        name: String,
        value: T,
        localizationsBuilder: ChoiceLocalizationsBuilder.() -> Unit = { }
    ) {
        val builder = ChoiceLocalizationsBuilder(name).apply(localizationsBuilder)
        if (choices == null) choices = mutableListOf()
        (choices ?: return).add(Choice.StringChoice(builder.name, Optional(builder.nameLocalizations), stringFormat.encodeToString(value)))
    }

    /**
     * converts the builder to a [BaseChoiceBuilder]
     */
    fun toBaseChoiceBuilder(): BaseChoiceBuilder<String>.() -> Unit = {
        default = this@CustomArgumentBuilder.default
        required = this@CustomArgumentBuilder.required
        autocomplete = this@CustomArgumentBuilder.autocomplete
        this@CustomArgumentBuilder.choices?.forEach {
            choice(it.name, it.value, it.nameLocalizations)
        }
    }

    override fun toRequest(): ApplicationCommandOption = ApplicationCommandOption(
        ApplicationCommandOptionType.String,
        name,
        _nameLocalizations,
        description,
        _descriptionLocalizations,
        _default,
        _required,
        autocomplete = _autocomplete
    )
}