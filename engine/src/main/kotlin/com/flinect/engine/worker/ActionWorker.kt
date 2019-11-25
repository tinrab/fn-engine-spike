package com.flinect.engine.worker

import com.flinect.engine.Context
import com.flinect.engine.Engine
import com.flinect.engine.Router
import com.flinect.engine.Worker
import kotlinx.coroutines.CoroutineScope

class ActionWorker(
    scope: CoroutineScope,
    engine: Engine
) : Worker(scope, engine) {
    private val triggerCommand = getCommand("action", "trigger")
    private val triggeredEvent = getEvent("action", "triggered")

    override fun buildRouter(): Router {
        return Router()
            .on(triggerCommand, this::handleTrigger)
    }

    private fun handleTrigger(context: Context) {
        trigger(context, triggeredEvent)
    }
}
