package com.flinect.graph

@GraphDsl
class SchemaBuilder private constructor() {
    private val nodes = HashMap<String, Node>()

    fun gate(id: String, f: GateBuilder.() -> Unit = {}) {
        require(!nodes.containsKey(id)) { "Node id '$id' is taken." }
        nodes[id] = GateBuilder.create(id, f)
    }

    fun structure(id: String, f: StructureBuilder.() -> Unit = {}) {
        require(!nodes.containsKey(id)) { "Node id '$id' is taken." }
        nodes[id] = StructureBuilder.create(id, f)
    }

    private fun build() = Schema(nodes)

    companion object {
        fun create(f: SchemaBuilder.() -> Unit): Schema {
            return SchemaBuilder().apply(f).build()
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

    private fun build() = Gate(id, properties)

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

    private fun build() = Structure(id, properties)

    companion object {
        fun create(id: String, f: StructureBuilder.() -> Unit): Structure {
            return StructureBuilder(id).apply(f).build()
        }
    }
}
