package com.flinect.engine

import com.flinect.graph.NodeId
import com.flinect.graph.PropertyId
import com.flinect.graph.value.Value

typealias CommandHandler = (context: Context) -> Unit

typealias DataProvider = (context: Context) -> Value

class Router {
    private val commandHandlers = HashMap<String, CommandHandler>()
    private val dataProviders = HashMap<String, DataProvider>()

    fun on(command: CommandReference, handler: CommandHandler): Router {
        commandHandlers["${command.node.id}#${command.property.id}"] = handler
        return this
    }

    fun provide(nodeId: NodeId, propertyId: PropertyId, dataProvider: DataProvider): Router {
        dataProviders["$nodeId#$propertyId"] = dataProvider
        return this
    }

    fun getCommandHandler(nodeId: NodeId, commandId: PropertyId): CommandHandler? {
        return commandHandlers["$nodeId#$commandId"]
    }
}
