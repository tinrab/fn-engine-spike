package com.flinect.engine.worker

import com.flinect.engine.Context
import com.flinect.engine.Engine
import com.flinect.engine.Router
import com.flinect.engine.Worker
import com.flinect.graph.value.Value
import kotlinx.coroutines.CoroutineScope

class IntegerWorker(
    scope: CoroutineScope,
    engine: Engine
) : Worker(scope, engine) {
    override fun buildRouter(): Router {
        return Router()
            .provide("integer", "return-value", this::handleInteger)
            .provide("plus", "c", this::handlePlus)
            .provide("minus", "c", this::handleMinus)
    }

    private fun handleInteger(context: Context): Value {
        return Value.of(1)
    }

    private fun handlePlus(context: Context): Value {
//        val a = readInput()
//        val b = readInput()
        return Value.of(2)
    }

    private fun handleMinus(context: Context): Value {
//        val a = readInput()
//        val b = readInput()
        return Value.of(3)
    }
}
