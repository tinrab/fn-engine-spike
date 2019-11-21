package com.flinect.graph

data class Instance(
    val node: Node,
    val key: String,
    val propertyValues: Map<String, PropertyValue>
)
