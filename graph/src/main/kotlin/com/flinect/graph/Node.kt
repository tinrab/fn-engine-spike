package com.flinect.graph

typealias NodeId = String

class Node(
    val id: NodeId,
    val properties: Map<PropertyId, Property>
) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is Node) return false

        if (id != other.id) return false

        return true
    }

    override fun hashCode(): Int {
        return id.hashCode()
    }
}
