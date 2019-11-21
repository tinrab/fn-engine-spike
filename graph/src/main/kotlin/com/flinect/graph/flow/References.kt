package com.flinect.graph.flow

import com.flinect.graph.Gate
import com.flinect.graph.Property
import com.flinect.graph.Structure

interface InstanceReference {
    val nodeInstance: NodeInstance
}

class GateReference internal constructor(
    val gateInstance: GateInstance,
    val gate: Gate
) : InstanceReference {
    override val nodeInstance: NodeInstance
        get() = gateInstance
}

class StructureReference internal constructor(
    val structureInstance: StructureInstance,
    val structure: Structure
) : InstanceReference {
    override val nodeInstance: NodeInstance
        get() = structureInstance
}

class PropertyReference internal constructor(
    val instance: InstanceReference,
    val property: Property
)

class EdgeReference internal constructor(
    val edge: Edge
)
