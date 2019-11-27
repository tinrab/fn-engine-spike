package com.flinect.engine.worker

import com.flinect.engine.Context
import com.flinect.engine.Engine
import com.flinect.engine.Router
import com.flinect.engine.Worker
import kotlinx.coroutines.CoroutineScope

class PrinterWorker(
    scope: CoroutineScope,
    engine: Engine
) : Worker(scope, engine) {
    private val contentInput = getInput("printer", "content")
    private val printCommand = getCommand("printer", "print")

    override fun buildRouter(): Router {
        return Router()
            .on(printCommand, this::handlePrint)
    }

    private fun handlePrint(context: Context) {
        val content = readInput(context, contentInput)
    }
}
