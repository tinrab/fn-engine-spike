package com.flinect.graph

class NodeInstance(
    val node: Node,
    val key: String,
    val values: Map<PropertyId, PropertyValue>
) {
    override fun toString(): String {
        return "${node.id}@$key"
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is NodeInstance) return false

        if (key != other.key) return false

        return true
    }

    override fun hashCode(): Int {
        return key.hashCode()
    }
}
