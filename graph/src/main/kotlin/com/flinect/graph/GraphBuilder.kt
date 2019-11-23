package com.flinect.graph

import com.flinect.graph.value.Value

@GraphDsl
class GraphBuilder private constructor(
    val schema: Schema
) {
    private val nodes = HashMap<String, NodeInstance>()
    private val edges = EdgeMap()

    private val propertyToNodeInstance = HashMap<Property, NodeInstance>()

    fun gate(id: NodeId, key: String, f: GateInstanceBuilder.() -> Unit = {}): GateData {
        require(!nodes.containsKey(key)) { "Key '$key' is taken." }

        val gate = GateInstanceBuilder.create(schema, id, key, f)
        nodes[key] = gate

        return gate
    }

    fun structure(id: NodeId, key: String, f: StructureInstanceBuilder.() -> Unit = {}): StructureData {
        require(!nodes.containsKey(key)) { "Key '$key' is taken." }

        val structure =
            StructureInstanceBuilder.create(schema, id, key, f)
        nodes[key] = structure

        return structure
    }

    fun property(nodeKey: String, propertyId: PropertyId): Property {
        val nodeInstance = nodes[nodeKey]
        requireNotNull(nodeInstance) { "Node instance with key '$nodeKey' not found." }
        return property(nodeInstance, propertyId)
    }

    fun property(nodeInstance: NodeInstance, propertyId: PropertyId): Property {
        val node = schema.nodes[nodeInstance.id]
        requireNotNull(node) { "Node with id '${nodeInstance.id}' not found." }

        val property = when (node) {
            is Gate -> node.properties
            is Structure -> node.properties
            else -> throw IllegalArgumentException("No properties defined on node '${nodeInstance.id}'")
        }[propertyId]
        requireNotNull(property) { "Property with id '$propertyId' not found" }

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
        val nodeInstances = nodes
            .mapValues {
                when (val node = it.value) {
                    is GateData -> {
                        return@mapValues GateInstance(
                            node.gate,
                            node.key,
                            node.values,
                            getInputs(node),
                            getOutputs(node)
                        )
                    }
                    is StructureData -> {
                        return@mapValues StructureInstance(
                            node.structure,
                            node.key,
                            node.values,
                            getInputs(node),
                            getOutputs(node)
                        )
                    }
                    else -> throw IllegalStateException("Unknown node class.")
                }
            }

        return Graph(nodeInstances, edges)
    }

    private fun getInputs(node: NodeInstance): Map<PropertyId, Hook> {
        return when (node) {
            is GateData -> node.gate.properties.values
            is StructureData -> node.structure.properties.values
            else -> throw IllegalStateException("Unknown node class.")
        }.map { it.id to edges.getInput(Hook(node, it))?.source }
            .filter { it.second != null }
            .map { it.first to it.second as Hook }
            .toMap()
    }

    private fun getOutputs(node: NodeInstance): Map<PropertyId, List<Hook>> {
        return when (node) {
            is GateData -> node.gate.properties.values
            is StructureData -> node.structure.properties.values
            else -> throw IllegalStateException("Unknown node class.")
        }.map { it.id to edges.getOutputs(Hook(node, it)).map { edge -> edge.target } }
            .filter { it.second.isNotEmpty() }
            .toMap()
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
class GateInstanceBuilder private constructor(
    schema: Schema,
    private val id: NodeId,
    private val key: String
) {
    private val gate: Gate
    private val values = HashMap<PropertyId, PropertyValue>()

    init {
        val g = schema.nodes[id]
        require(g != null && g is Gate) { "Gate '$id' does not exist." }
        gate = g
    }

    fun property(propertyId: PropertyId): Property {
        val property = gate.properties[propertyId]
        requireNotNull(property) { "Property with id '$propertyId' does not exist on '$id' gate." }
        return property
    }

    infix fun Property.assign(value: Value) {
        require(this is DataProperty) { "Cannot assign a value to '${this.id}' property of gate '$id'." }
        values[this.id] = PropertyValue(this, value)
    }

    private fun build() = GateData(gate, key, values)

    companion object {
        fun create(g: Schema, id: NodeId, key: String, f: GateInstanceBuilder.() -> Unit): GateData {
            return GateInstanceBuilder(g, id, key).apply(f).build()
        }
    }
}

@GraphDsl
class StructureInstanceBuilder private constructor(
    schema: Schema,
    private val id: NodeId,
    private val key: String
) {
    private val structure: Structure
    private val values = HashMap<PropertyId, PropertyValue>()

    init {
        val s = schema.nodes[id]
        require(s != null && s is Structure) { "Structure '$id' does not exist." }
        structure = s
    }

    fun property(propertyId: PropertyId): Property {
        val property = structure.properties[propertyId]
        requireNotNull(property) { "Property with id '$propertyId' does not exist on '$id' structure." }
        return property
    }

    infix fun Property.assign(value: Value) {
        require(this is DataProperty) { "Cannot assign a value to '${this.id}' property of gate '$id'." }
        values[this.id] = PropertyValue(this, value)
    }

    private fun build() = StructureData(structure, key, values)

    companion object {
        fun create(g: Schema, id: NodeId, key: String, f: StructureInstanceBuilder.() -> Unit): StructureData {
            return StructureInstanceBuilder(g, id, key).apply(f).build()
        }
    }
}

class StructureData(
    val structure: Structure,
    key: String,
    val values: Map<PropertyId, PropertyValue>
) : NodeInstance(structure.id, key)

class GateData(
    val gate: Gate,
    key: String,
    val values: Map<PropertyId, PropertyValue>
) : NodeInstance(gate.id, key)
