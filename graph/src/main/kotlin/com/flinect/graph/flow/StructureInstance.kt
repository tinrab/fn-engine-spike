package com.flinect.graph.flow

class StructureInstance(
    id: String,
    key: String,
    val propertyValues: Map<String, PropertyValue>
) : NodeInstance(id, key)
