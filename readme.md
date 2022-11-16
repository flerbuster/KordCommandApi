# KordCommandApi
## junge als würde das jemand benutzen

``` kotlin
data class Fler(val type: Int, val fler: String)
class ExceptionThatMightHappen : Exception("fler")

val flerCommand = slashCommand("fler", "fler",  kord) {
    basicArgument<User>("user", "user who gets flered") {
        required = true
    }

    customArgument("fler", "type of fler") {
        choice("fler1", Fler(1, "das ist hier ist fler 1"))
        choice("fler2", Fler(2, "das ist hier ist fler 2"))
        choice("fler3", Fler(3, "das ist hier ist fler 3"))


        required = true
    }

    runs { interaction, options ->
        val user = options.users["user"]!!
        val fler = options.custom<Fler>()["fler"]
        if (Random.nextBoolean()) throw ExceptionThatMightHappen()

        interaction.respondPublic {
            content = "hm ${user.mention} wurde geflert"
        }
    }

    catches<ExceptionThatMightHappen> { exception, interaction, command ->
        interaction.respondEphemeral { content = "hm jop exception passiert" }
    }
}
```

das sollte so gehen