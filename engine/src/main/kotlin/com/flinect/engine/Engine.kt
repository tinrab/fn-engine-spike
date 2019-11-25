package com.flinect.engine

import com.flinect.engine.config.EngineConfig
import com.flinect.engine.worker.ActionWorker
import com.flinect.engine.worker.IntegerWorker
import com.flinect.engine.worker.PrinterWorker
import com.flinect.engine.worker.RepeatWorker
import com.flinect.graph.Graph
import com.flinect.graph.Schema
import kotlinx.coroutines.*
import kotlinx.coroutines.channels.Channel
import kotlin.coroutines.CoroutineContext

class Engine(
    private val engineConfig: EngineConfig,
    val schema: Schema
) : CoroutineScope {
    private val _coroutineContext: CoroutineContext
    private val outboundChannels = ArrayList<Channel<Instruction>>()

    override val coroutineContext: CoroutineContext
        get() = _coroutineContext

    init {
        _coroutineContext = newFixedThreadPoolContext(3, "Engine")
    }

    fun run() {
        startWorker { scope -> IntegerWorker(scope, this) }
        startWorker { scope -> PrinterWorker(scope, this) }
        startWorker { scope -> RepeatWorker(scope, this) }
        startWorker { scope -> ActionWorker(scope, this) }
    }

    fun execute(graph: Graph) = runBlocking {
        delay(1000)
        repeat(100000000) {
            val context = Context(
                graph = graph,
                currentNode = graph.nodes["a1"]!!,
                tracer = Tracer()
            )
            broadcast(Instruction(context, "trigger"))
            delay(3000)
        }

        delay(1000000000000000000)
    }

    internal fun broadcast(instruction: Instruction) {
        launch {
            for (outbound in outboundChannels) {
                instruction.context.tracer.sentToWorker(instruction)
                outbound.send(instruction)
            }
        }
    }

    private fun startWorker(factory: (scope: CoroutineScope) -> Worker) {
        val inbound = Channel<Instruction>()
        outboundChannels.add(inbound)

        repeat(engineConfig.workerConfig.poolSize) {
            launch {
                factory(this).start(inbound)
            }
        }
    }
}
