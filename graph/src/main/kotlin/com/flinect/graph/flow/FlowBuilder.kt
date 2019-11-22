package com.flinect.graph.flow

import com.flinect.graph.CommandProperty
import com.flinect.graph.DataProperty
import com.flinect.graph.Direction
import com.flinect.graph.EventProperty
import com.flinect.graph.Gate
import com.flinect.graph.Graph
import com.flinect.graph.GraphDsl
import com.flinect.graph.NodeId
import com.flinect.graph.Property
import com.flinect.graph.PropertyId
import com.flinect.graph.Structure
import com.flinect.graph.value.Value

@GraphDsl
class FlowBuilder private constructor(
    val graph: Graph
) {
    private val instances = HashMap<String, NodeInstance>()
    private val edges = EdgeMap()

    private val propertyToNodeInstance = HashMap<Property, NodeInstance>()

    fun gate(id: NodeId, key: String, f: GateInstanceBuilder.() -> Unit = {}): GateData {
        require(!instances.containsKey(key)) { "Key '$key' is taken." }

        val gate = GateInstanceBuilder.create(graph, id, key, f)
        instances[key] = gate

        return gate
    }

    fun structure(id: NodeId, key: String, f: StructureInstanceBuilder.() -> Unit = {}): StructureData {
        require(!instances.containsKey(key)) { "Key '$key' is taken." }

        val structure = StructureInstanceBuilder.create(graph, id, key, f)
        instances[key] = structure

        return structure
    }

    fun property(nodeInstance: NodeInstance, propertyId: PropertyId): Property {
        val node = graph.nodes[nodeInstance.id]
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

    infix fun Property.connect(target: Property): Edge {
        val source = this
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

    private fun build(): Flow {
        val decoratedInstances = instances
            .mapValues {
                when (val node = it.value) {
                    is GateData -> {
                        return@mapValues GateInstance(
                            node.gate,
                            node.key,
                            node.propertyValues,
                            getInputs(node),
                            getOutputs(node)
                        )
                    }
                    is StructureData -> {
                        return@mapValues StructureInstance(
                            node.structure,
                            node.key,
                            node.propertyValues,
                            getInputs(node),
                            getOutputs(node)
                        )
                    }
                    else -> throw IllegalStateException("Unknown node class.")
                }
            }

        return Flow(decoratedInstances, edges)
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
        fun create(g: Graph, f: FlowBuilder.() -> Unit): Flow {
            return FlowBuilder(g).apply(f).build()
        }
    }
}

@GraphDsl
class GateInstanceBuilder private constructor(
    graph: Graph,
    private val id: NodeId,
    private val key: String
) {
    private val gate: Gate
    private val propertyValues = HashMap<PropertyId, PropertyValue>()

    init {
        val g = graph.nodes[id]
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
        propertyValues[this.id] = PropertyValue(this, value)
    }

    private fun build() = GateData(gate, key, propertyValues)

    companion object {
        fun create(g: Graph, id: NodeId, key: String, f: GateInstanceBuilder.() -> Unit): GateData {
            return GateInstanceBuilder(g, id, key).apply(f).build()
        }
    }
}

@GraphDsl
class StructureInstanceBuilder private constructor(
    graph: Graph,
    private val id: NodeId,
    private val key: String
) {
    private val structure: Structure
    private val propertyValues = HashMap<PropertyId, PropertyValue>()

    init {
        val s = graph.nodes[id]
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
        propertyValues[this.id] = PropertyValue(this, value)
    }

    private fun build() = StructureData(structure, key, propertyValues)

    companion object {
        fun create(g: Graph, id: NodeId, key: String, f: StructureInstanceBuilder.() -> Unit): StructureData {
            return StructureInstanceBuilder(g, id, key).apply(f).build()
        }
    }
}

class StructureData(
    val structure: Structure,
    key: String,
    val propertyValues: Map<PropertyId, PropertyValue>
) : NodeInstance(structure.id, key)

class GateData(
    val gate: Gate,
    key: String,
    val propertyValues: Map<PropertyId, PropertyValue>
) : NodeInstance(gate.id, key)
