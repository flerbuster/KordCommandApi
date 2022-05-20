package commandApi.listen

import kotlinx.coroutines.*
import kotlinx.coroutines.selects.SelectClause0
import kotlin.coroutines.CoroutineContext

interface KordListenerInterface : Job {
    val job: Job

    fun unregister() {
        job.cancel()
    }

    fun register() {
        job.start()
    }
}

class KordListener(override val job: Job) : KordListenerInterface {
    override val children: Sequence<Job>
        get() = job.children
    override val isActive: Boolean
        get() = job.isActive
    override val isCancelled: Boolean
        get() = job.isCancelled
    override val isCompleted: Boolean
        get() = job.isCompleted
    override val key: CoroutineContext.Key<*>
        get() = job.key
    override val onJoin: SelectClause0
        get() = job.onJoin

    @Suppress("DEPRECATION_ERROR")
    @InternalCoroutinesApi
    override fun attachChild(child: ChildJob): ChildHandle {
        return job.attachChild(child)
    }

    @Deprecated(level = DeprecationLevel.HIDDEN, message = "Since 1.2.0, binary compatibility with versions <= 1.1.x")
    override fun cancel(cause: Throwable?): Boolean {
        job.cancel()
        return job.isCancelled
    }

    override fun cancel(cause: CancellationException?) {
        return job.cancel(cause)
    }

    @InternalCoroutinesApi
    override fun getCancellationException(): CancellationException {
        return job.getCancellationException()
    }

    @InternalCoroutinesApi
    override fun invokeOnCompletion(
        onCancelling: Boolean,
        invokeImmediately: Boolean,
        handler: CompletionHandler
    ): DisposableHandle {
        return job.invokeOnCompletion(onCancelling, invokeImmediately, handler)
    }

    override fun invokeOnCompletion(handler: CompletionHandler): DisposableHandle {
        return job.invokeOnCompletion(handler)
    }

    override suspend fun join() {
        return job.join()
    }

    override fun start(): Boolean {
        return job.start()
    }
}