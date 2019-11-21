package com.flinect.graph

@GraphDsl
class GraphBuilder private constructor() {
    private val nodes = HashMap<String, Node>()

    fun node(id: String, f: NodeBuilder.() -> Unit) {
        require(!nodes.containsKey(id)) { "Node id '$id' is taken." }
        nodes[id] = NodeBuilder(id).apply(f).build()
    }

    fun build() = Graph(nodes)

    companion object {
        fun create(f: GraphBuilder.() -> Unit): Graph {
            return GraphBuilder().apply(f).build()
        }
    }
}

@GraphDsl
class NodeBuilder(private val id: String) {
    private val properties = HashMap<String, Property>()

    fun input(id: String, type: DataType, f: PropertyBuilder.() -> Unit = {}) {
        val property = PropertyBuilder(id, Property.Kind.INPUT, type).apply(f).build()
        validateNewProperty(property)
        properties[id] = property
    }

    fun output(id: String, type: DataType, f: PropertyBuilder.() -> Unit = {}) {
        val property = PropertyBuilder(id, Property.Kind.OUTPUT, type).apply(f).build()
        validateNewProperty(property)
        properties[id] = property
    }

    fun event(id: String, f: PropertyBuilder.() -> Unit = {}) {
        val property = PropertyBuilder(id, Property.Kind.EVENT).apply(f).build()
        validateNewProperty(property)
        properties[id] = property
    }

    fun command(id: String, f: PropertyBuilder.() -> Unit = {}) {
        val property = PropertyBuilder(id, Property.Kind.COMMAND).apply(f).build()
        validateNewProperty(property)
        properties[id] = property
    }

    private fun validateNewProperty(property: Property) {
        require(!properties.containsKey(property.id)) { "Property id '${property.id}' is taken." }
        if (property.isControl()) {
            require(property.type == null) { "Control property '${property.id}' cannot have a type." }
        } else if (property.isData()) {
            require(property.type != null) { "Data property '${property.id}' must have a type." }
        }
    }

    fun build() = Node(id, properties)
}

@GraphDsl
class PropertyBuilder(
    private val id: String,
    private val kind: Property.Kind,
    private val type: DataType? = null
) {
    var connectable: Boolean = true

    fun build() = Property(id, kind, type, connectable)
}
