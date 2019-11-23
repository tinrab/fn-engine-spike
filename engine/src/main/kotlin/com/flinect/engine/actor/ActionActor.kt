package com.flinect.engine.actor

import com.flinect.engine.Actor
import com.flinect.engine.Receiver
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.runBlocking

class ActionActor(scope: CoroutineScope) : Actor(scope) {
    override fun buildReceiver(): Receiver {
        val r = Receiver()
//        r.on(PingMessage::class, this::handlePing)
        return r
    }

//    private fun handlePing(message: PingMessage) = runBlocking {
//        println("$ID($actorId): PONG")
//    }

    companion object {
        const val ID = "action"
        const val EVENT_ON_EXECUTE = "on-execute"
    }
}
