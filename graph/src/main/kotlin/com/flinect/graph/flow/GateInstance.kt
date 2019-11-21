package com.flinect.graph.flow

class GateInstance(
    id: String,
    key: String,
    val propertyValues: Map<String, PropertyValue>
) : NodeInstance(id, key)
