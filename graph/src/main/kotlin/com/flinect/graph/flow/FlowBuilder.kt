package com.flinect.graph.flow

import com.flinect.graph.CommandProperty
import com.flinect.graph.DataProperty
import com.flinect.graph.Direction
import com.flinect.graph.EventProperty
import com.flinect.graph.Gate
import com.flinect.graph.Graph
import com.flinect.graph.GraphDsl
import com.flinect.graph.Property
import com.flinect.graph.Structure

@GraphDsl
class FlowBuilder private constructor(
    val graph: Graph
) {
    private val instances = HashMap<String, NodeInstance>()
    private val edges = HashMap<String, HashMap<String, Edge>>()

    fun gate(id: String, key: String, f: GateInstanceBuilder.() -> Unit = {}): InstanceReference {
        require(!instances.containsKey(key)) { "Key '$key' is taken." }
        val gateReference = GateInstanceBuilder.create(graph, id, key, f)
        instances[key] = gateReference.nodeInstance
        return gateReference
    }

    fun structure(id: String, key: String, f: StructureInstanceBuilder.() -> Unit = {}): InstanceReference {
        require(!instances.containsKey(key)) { "Key '$key' is taken." }
        val structureReference = StructureInstanceBuilder.create(graph, id, key, f)
        instances[key] = structureReference.nodeInstance
        return structureReference
    }

    fun property(instanceReference: InstanceReference, propertyId: String): PropertyReference {
        val instance = instanceReference.nodeInstance
        val node = graph.nodes[instance.id]
        requireNotNull(node) { "Node with id '${instance.id}' not found." }
        val property = when (node) {
            is Gate -> node.properties
            is Structure -> node.properties
            else -> throw IllegalArgumentException("No properties defined on node '${instance.id}'")
        }[propertyId]
        requireNotNull(property) { "Property with id '$propertyId' not found" }
        return PropertyReference(instanceReference, property)
    }

    infix fun PropertyReference.connect(targetReference: PropertyReference): EdgeReference {
        val source = this.property
        val target = targetReference.property

        require(this.instance.nodeInstance.key != targetReference.instance.nodeInstance.key) {
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
            Hook(this.instance.nodeInstance, source.id),
            Hook(targetReference.instance.nodeInstance, target.id)
        )

        var outputMap = edges[edge.source.key]
        if (outputMap == null) {
            outputMap = HashMap()
            edges[edge.source.key] = outputMap
        }
        require(!outputMap.containsKey(edge.target.key)) { "Edge '$edge' already exists." }
        outputMap[edge.target.key] = edge

        return EdgeReference(edge)
    }

    private fun build(): Flow {
        return Flow(instances, edges)
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
    private val id: String,
    private val key: String
) {
    private val gate: Gate
    private val propertyValues = HashMap<String, PropertyValue>()

    init {
        val g = graph.nodes.filterValues { it is Gate }[id] as Gate?
        gate = requireNotNull(g) { "Gate '$id' does not exist." }
    }

    fun property(propertyId: String): Property {
        val property = gate.properties[propertyId]
        requireNotNull(property) { "Property with id '$propertyId' does not exist on '$id' gate." }
        return property
    }

    infix fun Property.assign(value: Value) {
        require(this is DataProperty) { "Cannot assign a value to '${this.id}' property of gate '$id'." }
        propertyValues[this.id] = PropertyValue(this.id, value)
    }

    private fun build() = GateReference(GateInstance(id, key, propertyValues), gate)

    companion object {
        fun create(g: Graph, id: String, key: String, f: GateInstanceBuilder.() -> Unit): GateReference {
            return GateInstanceBuilder(g, id, key).apply(f).build()
        }
    }
}

@GraphDsl
class StructureInstanceBuilder private constructor(
    graph: Graph,
    private val id: String,
    private val key: String
) {
    private val structure: Structure
    private val propertyValues = HashMap<String, PropertyValue>()

    init {
        val s = graph.nodes.filterValues { it is Structure }[id] as Structure?
        structure = requireNotNull(s) { "Structure '$id' does not exist." }
    }

    fun property(propertyId: String): Property {
        val property = structure.properties[propertyId]
        requireNotNull(property) { "Property with id '$propertyId' does not exist on '$id' structure." }
        return property
    }

    infix fun Property.assign(value: Value) {
        require(this is DataProperty) { "Cannot assign a value to '${this.id}' property of gate '$id'." }
        propertyValues[this.id] = PropertyValue(this.id, value)
    }

    private fun build() = StructureReference(StructureInstance(id, key, propertyValues), structure)

    companion object {
        fun create(g: Graph, id: String, key: String, f: StructureInstanceBuilder.() -> Unit): StructureReference {
            return StructureInstanceBuilder(g, id, key).apply(f).build()
        }
    }
}
