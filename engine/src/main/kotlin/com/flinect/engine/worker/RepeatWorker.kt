package com.flinect.engine.worker

import com.flinect.engine.Context
import com.flinect.engine.Engine
import com.flinect.engine.Router
import com.flinect.engine.Worker
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.runBlocking

class RepeatWorker(
    scope: CoroutineScope,
    engine: Engine
) : Worker(scope, engine) {
    private val startCommand = getCommand("repeat", "start")
    private val executedEvent = getEvent("repeat", "executed")

    override fun buildRouter(): Router {
        return Router()
            .on(startCommand, this::handleStart)
    }

    private fun handleStart(context: Context) = runBlocking {
        repeat(3) {
            trigger(context, executedEvent)
        }
    }
}
