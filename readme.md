# KordCommandApi
## junge als würde das jemand benutzen

``` kotlin
val flerCommand = slashCommand("fler", "fler",  kord) {
    argument<User>("user", "user who gets flered") {
        required = true
    }

    runs { interaction, options ->
        val user = options.users["user"]!!

        interaction.respondPublic {
            content = "hm ${user.mention} wurde geflert"
        }
    }
}
```

das sollte so gehen