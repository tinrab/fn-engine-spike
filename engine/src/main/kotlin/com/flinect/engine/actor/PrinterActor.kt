package com.flinect.engine.actor

import com.flinect.engine.Actor
import com.flinect.engine.Receiver
import kotlinx.coroutines.CoroutineScope

class PrinterActor(scope: CoroutineScope) : Actor(scope) {
    override fun buildReceiver(): Receiver {
        val r = Receiver()
        return r
    }

    companion object {
        const val ID = "printer"
        const val COMMAND_PRINT = "print"
    }
}
