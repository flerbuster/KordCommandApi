# Kord Command Api

## hier gehts zum [Download](https://github.com/flerbuster/KordCommandApi/releases/download/fler/KordCommandApi-0.1.8.7.jar)

####

> ## Installation Guide
> 1. Download The [Kord Command Api Jar](https://github.com/flerbuster/KordCommandApi/releases/download/fler/KordCommandApi-0.1.8.7.jar)
> 2. Create a Gradle project
> 3. Add the [Kordlib](https://github.com/kordlib/kord) Dependency (Maven Central)
>
>      ```dependencies { implementation("dev.kord:kord-core:0.10.0") }```
> 4. place the [Kord Command Api Jar](https://github.com/flerbuster/KordCommandApi/releases/download/fler/KordCommandApi-0.1.8.7.jar) in the project files /libs/
> 5. add the file dependency 
> 
>      ```dependencies { implementation(files("./libs/KordCommandApi-0.10.0.jar)) }```
> 6. enjoy

You can create slash commands like this
```kt
val exampleCommand = slashCommand("examplecommand", "this is an example command",  kord) {

}
```

You can now add arguments and what passiert when command gets executed
```kt
val exampleCommand = slashCommand("examplecommand", "this is an example command",  kord) {
    argument<ArgumentType>("argument name", "argument description") {
        required = true/false
    }
    or
    basicArgument<BasicArgumentType>("argument name", "argument description") {
        required = true/false
    }

    runs { interaction, options ->
        val example = options.ArgumentType(e.g. strings)["argument name"]

        interaction.respondPublic {
            content = example
        }
    }
}
```


>Argument Types can include String, Int, Number
>Basic Argument Types include Boolean, User, Membner, Mentionable, Channel, Role, SubCommand, Group, Attachment, you can **not** add choices to these, as they are given by discord

>## options
>#### `options` is always an instance of an subclass of `BaseOptions`.
>>### Abstract properties
>>- `strings`: Map<String, String>
>>- `integers`: Map<String, Long>
>>- `numbers`: Map<String, Double>
>>- `booleans`: Map<String, Boolean>
>>- `users`: Map<String, User>
>>- `members`: Map<String, Member>
>>- `channels`: Map<String, ResolvedChannel>
>>- `roles`: Map<String, Role>
>>- `mentionables`: Map<String, Entity>
>>- `attachments`: Map<String, Attachment>
>
>>### Abstract functions
>>- `inline fun <reified T> custom(stringFormat: StringFormat = Json): HashMap<String, T>`
>>- `operator fun <T : Comparable<T>> get(at: String): T?`
>>- `operator fun get(at: String): Comparable<*>?`

You can also add required choices for the user to arguments

```kt
argument<ArgumentType>("argument name", "argument description") {
    required = true/false

    choice("choice 1 (seen by user)", "choice1" (what gets passed to options, must be same type as argument))
}
```

if you do not want to be stuck with the normal argument types, you can also do custom ones, they need to be Serializable
```kt
@Serializable
data class ExampleCustomClass(
    stringValue: String,
    integerValue: Int
)

....
    customArgument<ExampleCustomClass>("argument name", "argument description") {
        required = true/false

        choice("choice 1 (seen by user)", ExampleCustomClass("value 1", 1))
        choice("choice 2 (seen by user)", ExampleCustomClass("value 2", 2))
    }
```
you can then get the value the user chose at runtime
```kt
runs { interaction, options ->
    val example = options.custom<ExampleCustomClass>()["argument name"] 
}
```

This is a complete example

```kt
import dev.kord.core.Kord
import dev.kord.core.behavior.interaction.respondEphemeral
import dev.kord.core.behavior.interaction.respondPublic
import dev.kord.core.entity.User
import kotlinx.coroutines.runBlocking
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json
import kotlin.random.Random

@Serializable
data class Fler(val type: Int, val fler: String)

class ExceptionThatMightHappen : Exception("fler")

val flerCommand = slashCommand("fler", "fler",  Bot.kord) {
    val json = Json {
        ignoreUnknownKeys = true
        prettyPrint = true
        prettyPrintIndent = " ".repeat(187)
    }

    basicArgument<User>("user", "user who gets flered") {
        required = true
    }

    customArgument("fler", "type of fler") {
        choice("fler1", Fler(1, "das ist hier ist fler 1"), json)
        choice("fler2", Fler(2, "das ist hier ist fler 2"), json)
        choice("fler3", Fler(3, "das ist hier ist fler 3"), json)

        required = true
    }

    runs { interaction, options ->
        val user = options.users["user"]!!
        val fler = options.custom<Fler>(json)["fler"]!!
        if (Random.nextBoolean()) throw ExceptionThatMightHappen()

        interaction.respondPublic {
            content = "hm ${user.mention} wurde mit $fler geflert"
        }
    }

    catches<ExceptionThatMightHappen> { exception, interaction, command ->
        interaction.respondEphemeral { content = "hm jop exception passiert" }
    }
}

object Bot {
    val kord = runBlocking { Kord("TOKEN") }

    suspend fun run() {
        flerCommand

        kord.login()
    }
}

suspend fun main() {
    Bot.run()
}
```