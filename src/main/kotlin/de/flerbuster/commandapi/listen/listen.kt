package commandApi.listen

import dev.kord.core.Kord
import dev.kord.core.event.Event
import dev.kord.core.on
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job

inline fun <reified T: Event> listen(
    kord: Kord,
    newScope: CoroutineScope = CoroutineScope(Dispatchers.Default),
    noinline onEvent: suspend T.() -> Unit
): KordListener {
    return kord.on(newScope, onEvent).toKordListener()
}

fun Job.toKordListener(): KordListener = KordListener(this)