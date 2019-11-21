package com.flinect.graph

@GraphDsl
class GraphBuilder private constructor() {
    private val nodes = HashMap<String, Node>()

    fun gate(id: String, f: GateBuilder.() -> Unit) {
        require(!nodes.containsKey(id)) { "Node id '$id' is taken." }
        nodes[id] = GateBuilder.create(id, f)
    }

    fun structure(id: String, f: StructureBuilder.() -> Unit) {
        require(!nodes.containsKey(id)) { "Node id '$id' is taken." }
        nodes[id] = StructureBuilder.create(id, f)
    }

    fun build() = Graph(nodes)

    companion object {
        fun create(f: GraphBuilder.() -> Unit): Graph {
            return GraphBuilder().apply(f).build()
        }
    }
}

@GraphDsl
class GateBuilder(private val id: String) {
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
        require(!properties.containsKey(property.id)) { "Property id '${property.id}' is taken." }
    }

    fun build() = Gate(id, properties)

    companion object {
        fun create(id: String, f: GateBuilder.() -> Unit): Gate {
            return GateBuilder(id).apply(f).build()
        }
    }
}

@GraphDsl
class StructureBuilder(private val id: String) {
    private val properties = HashMap<String, DataProperty>()

    fun property(id: String, type: DataType) {
        val property = DataProperty(id, Direction.OUT, type)
        require(!properties.containsKey(property.id)) { "Property id '${property.id}' is taken." }
        properties[id] = property
    }

    fun build() = Structure(id, properties)

    companion object {
        fun create(id: String, f: StructureBuilder.() -> Unit): Structure {
            return StructureBuilder(id).apply(f).build()
        }
    }
}
