package com.flinect.graph

import com.flinect.graph.value.Value

@GraphDsl
class GraphBuilder private constructor(
    val schema: Schema
) {
    private val nodes = HashMap<String, NodeInstance>()
    private val edges = EdgeMap()

    private val propertyToNodeInstance = HashMap<Property, NodeInstance>()

    fun node(id: NodeId, key: String, f: NodeInstanceBuilder.() -> Unit = {}): NodeInstance {
        require(!nodes.containsKey(key)) { "Key '$key' is taken." }

        val node = NodeInstanceBuilder.create(schema, id, key, f)
        nodes[key] = node

        return node
    }

    fun property(nodeKey: String, propertyId: PropertyId): Property {
        val nodeInstance = nodes[nodeKey]
        requireNotNull(nodeInstance) { "Node instance with key '$nodeKey' not found." }
        return property(nodeInstance, propertyId)
    }

    fun property(nodeInstance: NodeInstance, propertyId: PropertyId): Property {
        val node = schema.nodes[nodeInstance.node.id]
        requireNotNull(node) { "Node with id '${nodeInstance.node.id}' not found." }

        val property = requireNotNull(node.properties[propertyId]) {
            "Property with id '$propertyId' not found"
        }

        propertyToNodeInstance[property] = nodeInstance

        return property
    }

    fun connect(source: Property, target: Property): Edge {
        val sourceInstance = propertyToNodeInstance[source]
        val targetInstance = propertyToNodeInstance[target]
        requireNotNull(sourceInstance) { "Invalid source property '${source.id}'." }
        requireNotNull(targetInstance) { "Invalid target property '${target.id}'." }

        require(sourceInstance.key != targetInstance.key) {
            "Cannot connect to self."
        }

        if (source.direction != Direction.OUT || source is CommandProperty) {
            throw IllegalArgumentException("Invalid source property.")
        }
        if (target.direction != Direction.IN || target is EventProperty) {
            throw IllegalArgumentException("Invalid target property.")
        }
        if (source is EventProperty && target !is CommandProperty) {
            throw IllegalArgumentException("Event can only be hooked to a command.")
        }
        if (target is CommandProperty && source !is EventProperty) {
            throw IllegalArgumentException("Command can only be triggered by an event.")
        }
        if (source is DataProperty && target is DataProperty) {
            require(source.type == target.type) { "Incompatible types." }
        }

        val edge = Edge(
            Hook(sourceInstance, source),
            Hook(targetInstance, target)
        )

        require(!edges.contains(edge)) { "Edge '${edge}' already exists." }
        edges.put(edge)

        return edge
    }

    infix fun Property.connectTo(target: Property): Edge {
        return connect(this, target)
    }

    fun build(): Graph {
        return Graph(nodes, edges)
    }

    companion object {
        fun create(schema: Schema, f: GraphBuilder.() -> Unit): Graph {
            return GraphBuilder(schema).apply(f).build()
        }

        fun create(schema: Schema): GraphBuilder {
            return GraphBuilder(schema)
        }
    }
}

@GraphDsl
class NodeInstanceBuilder private constructor(
    schema: Schema,
    private val id: NodeId,
    private val key: String
) {
    private val node: Node = requireNotNull(schema.nodes[id]) { "Node '$id' does not exist." }
    private val values = HashMap<PropertyId, PropertyValue>()

    fun property(propertyId: PropertyId): Property {
        val property = node.properties[propertyId]
        requireNotNull(property) { "Property with id '$propertyId' does not exist on '$id' gate." }
        return property
    }

    infix fun Property.assign(value: Value) {
        require(this is DataProperty) { "Cannot assign a value to '${this.id}' property of gate '$id'." }
        values[this.id] = PropertyValue(this, value)
    }

    private fun build() = NodeInstance(node, key, values)

    companion object {
        fun create(g: Schema, id: NodeId, key: String, f: NodeInstanceBuilder.() -> Unit): NodeInstance {
            return NodeInstanceBuilder(g, id, key).apply(f).build()
        }
    }
}
