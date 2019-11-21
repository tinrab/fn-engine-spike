package com.flinect.graph

@GraphDsl
class FlowBuilder private constructor(
    private val g: Graph
) {
    private val nodes = HashMap<String, Instance>()

    fun addNode(id: String, key: String, f: NodeDeclarer.() -> Unit = {}) {
        require(!nodes.containsKey(key)) { "Node key '$key' is taken." }
        nodes[key] = NodeDeclarer(g, id, key).apply(f).build()
    }

    fun build(): Flow {
        return Flow(nodes)
    }

    companion object {
        fun with(g: Graph, f: FlowBuilder.() -> Unit): Flow {
            return FlowBuilder(g).apply(f).build()
        }
    }
}

@GraphDsl
class NodeDeclarer(
    g: Graph,
    val id: String,
    val key: String
) {
    private val node: Node
    private val propertyValues = HashMap<String, PropertyValue>()

    init {
        val n = g.nodes[id]
        requireNotNull(n) { "Node '$id' does not exist." }
        node = n
    }

    fun setProperty(id: String, value: Value) {
        val property = node.properties[id]
        requireNotNull(property) { "Property '$id' does not exist on node '${node.id}'" }
        require(value.type == property.type) { "Incompatible types." }

        propertyValues[id] = PropertyValue(node, property, value)
    }

    fun build() = Instance(node, key, propertyValues)
}
