package com.flinect.graph

class Gate(
    id: NodeId,
    val properties: Map<PropertyId, Property>
) : Node(id)
