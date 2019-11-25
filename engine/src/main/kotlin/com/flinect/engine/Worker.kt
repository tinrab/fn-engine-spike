package com.flinect.engine

import com.flinect.graph.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.channels.ReceiveChannel
import java.util.concurrent.atomic.AtomicLong

abstract class Worker(
    scope: CoroutineScope,
    private val engine: Engine
) : CoroutineScope by scope {
    val workerId = nextWorkerId.incrementAndGet()

    internal suspend fun start(inbound: ReceiveChannel<Instruction>) {
        val router = buildRouter()

        for (instruction in inbound) {
            instruction.context.tracer.workerReceivedInstruction(instruction)

            val handler = router.getCommandHandler(instruction.context.currentNode.node.id, instruction.commandId)
            if (handler != null) {
                instruction.context.tracer.instructionPassedToHandler(instruction, instruction.context.currentNode.node)
                handler(instruction.context)
            }
        }
    }

    protected abstract fun buildRouter(): Router

    protected open fun trigger(context: Context, event: EventReference) {
        context.tracer.eventTriggered(event)
        // Broadcast to outputs
        val outputs = context.graph.edges.getOutputs(Hook(context.currentNode, event.property))
        for (output in outputs) {
            val instruction = Instruction(
                context = context.copy(currentNode = output.nodeInstance),
                commandId = output.property.id
            )

            context.tracer.broadcastInstruction(instruction)
            engine.broadcast(instruction)
        }
    }

    protected fun getCommand(nodeId: NodeId, propertyId: PropertyId): CommandReference {
        val node = checkNotNull(engine.schema.nodes[nodeId])
        val property = checkNotNull(node.properties[propertyId])
        check(property is CommandProperty)
        return CommandReference(node, property)
    }

    protected fun getEvent(nodeId: NodeId, propertyId: PropertyId): EventReference {
        val node = checkNotNull(engine.schema.nodes[nodeId])
        val property = checkNotNull(node.properties[propertyId])
        check(property is EventProperty)
        return EventReference(node, property)
    }

    protected fun readInput(context: Context, propertyId: PropertyId) {
        
    }

    companion object {
        private var nextWorkerId = AtomicLong(0)
    }
}
