package com.flinect.graph

@GraphDsl
class SchemaBuilder private constructor() {
    private val nodes = HashMap<String, Node>()

    fun node(id: String, f: NodeBuilder.() -> Unit = {}) {
        require(GraphUtil.isValidId(id)) { "Invalid node id '$id'." }
        require(!nodes.containsKey(id)) { "Node id '$id' is taken." }
        nodes[id] = NodeBuilder.create(id, f)
    }

    private fun build() = Schema(nodes)

    companion object {
        fun create(f: SchemaBuilder.() -> Unit): Schema {
            return SchemaBuilder().apply(f).build()
        }
    }
}

@GraphDsl
class NodeBuilder(private val id: String) {
    private val properties = HashMap<String, Property>()

    fun input(id: String, type: DataType) {
        val property = DataProperty(id, Direction.IN, type)
        validateNewProperty(property)
        properties[id] = property
    }

    fun output(id: String, type: DataType) {
        val property = DataProperty(id, Direction.OUT, type)
        validateNewProperty(property)
        properties[id] = property
    }

    fun event(id: String) {
        val property = EventProperty(id)
        validateNewProperty(property)
        properties[id] = property
    }

    fun command(id: String) {
        val property = CommandProperty(id)
        validateNewProperty(property)
        properties[id] = property
    }

    private fun validateNewProperty(property: Property) {
        require(GraphUtil.isValidId(property.id)) { "Invalid property id '${property.id}'." }
        require(!properties.containsKey(property.id)) { "Property id '${property.id}' is taken." }
    }

    private fun build() = Node(id, properties)

    companion object {
        fun create(id: String, f: NodeBuilder.() -> Unit): Node {
            return NodeBuilder(id).apply(f).build()
        }
    }
}
