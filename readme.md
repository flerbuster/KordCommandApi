# KordCommandApi
## junge als würde das jemand benutzen

``` kotlin
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

das sollte so gehen