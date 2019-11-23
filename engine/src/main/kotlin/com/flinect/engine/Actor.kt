package com.flinect.engine

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.channels.ReceiveChannel
import java.util.concurrent.atomic.AtomicLong

abstract class Actor(
    scope: CoroutineScope
) : CoroutineScope by scope {
    val actorId = nextActorId.incrementAndGet()
    private lateinit var receiver: Receiver

    internal suspend fun run(inbox: ReceiveChannel<Message>) {
        receiver = buildReceiver()
        for (message in inbox) {
            val handler = receiver.getHandler<Message>(message::class) ?: continue
            handler(message)
        }
    }

    abstract fun buildReceiver(): Receiver

    companion object {
        private var nextActorId = AtomicLong(0)
    }
}
