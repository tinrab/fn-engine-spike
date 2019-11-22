package com.flinect.graph

class Structure(
    id: NodeId,
    val properties: Map<PropertyId, DataProperty>
) : Node(id)
