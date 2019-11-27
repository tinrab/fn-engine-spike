package com.flinect.engine

import com.flinect.graph.NodeId
import com.flinect.graph.PropertyId
import com.flinect.graph.value.Value

typealias CommandHandler = (context: Context) -> Unit

typealias ValueProvider = (context: Context) -> Value

class Router {
    private val commandHandlers = HashMap<String, CommandHandler>()
    private val valueProviders = HashMap<String, ValueProvider>()

    fun on(command: CommandReference, handler: CommandHandler): Router {
        commandHandlers["${command.node.id}#${command.property.id}"] = handler
        return this
    }

    fun provide(nodeId: NodeId, propertyId: PropertyId, valueProvider: ValueProvider): Router {
        valueProviders["$nodeId#$propertyId"] = valueProvider
        return this
    }

    fun getCommandHandler(nodeId: NodeId, commandId: PropertyId): CommandHandler? {
        return commandHandlers["$nodeId#$commandId"]
    }

    fun getValueProvider(nodeId: NodeId, propertyId: PropertyId): ValueProvider? {
        return valueProviders["$nodeId#$propertyId"]
    }
}
