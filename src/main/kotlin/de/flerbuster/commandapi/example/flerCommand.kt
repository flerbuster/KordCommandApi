package de.flerbuster.commandapi.example

import de.flerbuster.commandapi.builder.slashCommand
import dev.kord.core.Kord
import dev.kord.core.behavior.interaction.respondPublic
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.runBlocking

val kord = runBlocking { Kord("YOURTOKEN") }

@DelicateCoroutinesApi
val flerCommand = slashCommand("fler", "fler", kord) {
    onInit {
        println("fler command was added")
    }

    argument<Boolean>("fler", "do you like fler") {
        required = true
    }

    runs { interaction, options ->
        val doesLikeFler = options.booleans["fler"]!!

        if (doesLikeFler) {
            interaction.respondPublic {
                content = "${interaction.user.mention} likes fler :D"
            }
        } else {
            interaction.user.publicFlags
        }
    }
}