package com.flinect.engine

import com.flinect.graph.CommandProperty
import com.flinect.graph.DataProperty
import com.flinect.graph.Direction
import com.flinect.graph.EventProperty
import com.flinect.graph.Hook
import com.flinect.graph.NodeId
import com.flinect.graph.PropertyId
import com.flinect.graph.value.Value
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.channels.Channel
import java.util.concurrent.atomic.AtomicLong

abstract class Worker(
    scope: CoroutineScope,
    private val engine: Engine
) : CoroutineScope by scope {
    val workerId = nextWorkerId.incrementAndGet()
    private lateinit var router: Router

    internal suspend fun start(messageChannel: Channel<Message>) {
        router = buildRouter()

        for (message in messageChannel) {
            when (message) {
                is Instruction -> handleInstruction(message)
                is Query -> handleQuery(message)
                is QueryResulted -> handleQueryResulted(message)
            }
        }
    }

    private fun handleInstruction(instruction: Instruction) {
        val handler = router.getCommandHandler(instruction.context.currentNode.node.id, instruction.commandId)
        if (handler != null) {
            handler(instruction.context)
        }
    }

    private suspend fun handleQuery(query: Query) {
        val handler = router.getValueProvider(query.context.currentNode.node.id, query.propertyId)
        if (handler != null) {
            val value = handler(query.context)
            val result = QueryResulted(
                context = query.context.copy(currentNode = query.senderNode),
                value = value,
                senderWorkerId = query.senderWorkerId
            )
            engine.broadcast(result)
        }
    }

    private fun handleQueryResulted(queryResulted: QueryResulted) {
        // TODO: Maybe set values for other workers as well
        if (workerId != queryResulted.senderWorkerId) {
            return
        }
        println(queryResulted.value)
    }

    protected abstract fun buildRouter(): Router

    protected open fun trigger(context: Context, event: EventReference) {
        // Broadcast to outputs
        val outputs = context.graph.edges.getOutputs(Hook(context.currentNode, event.property))
        for (output in outputs) {
            val instruction = Instruction(
                context = context.copy(currentNode = output.nodeInstance),
                commandId = output.property.id
            )

            engine.broadcast(instruction)
        }
    }

    protected fun readInput(context: Context, input: InputReference): Value {
        val hook = context.graph.edges.getInput(Hook(context.currentNode, input.property))
        checkNotNull(hook)

        val query = Query(
            context = context.copy(currentNode = hook.nodeInstance),
            propertyId = hook.property.id,
            senderNode = context.currentNode,
            senderWorkerId = workerId
        )
        engine.broadcast(query)

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

    protected fun getInput(nodeId: NodeId, propertyId: PropertyId): InputReference {
        val node = checkNotNull(engine.schema.nodes[nodeId])
        val property = checkNotNull(node.properties[propertyId])
        check(property is DataProperty)
        check(property.direction == Direction.IN)
        return InputReference(node, property)
    }

    protected fun getOutput(nodeId: NodeId, propertyId: PropertyId): OutputReference {
        val node = checkNotNull(engine.schema.nodes[nodeId])
        val property = checkNotNull(node.properties[propertyId])
        check(property is DataProperty)
        check(property.direction == Direction.OUT)
        return OutputReference(node, property)
    }

    companion object {
        private var nextWorkerId = AtomicLong(0)
    }
}
