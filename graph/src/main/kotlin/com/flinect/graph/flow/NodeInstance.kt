package com.flinect.graph.flow

import com.flinect.graph.NodeId

abstract class NodeInstance(
    val id: NodeId,
    val key: String
) {
    override fun toString(): String {
        return "$id#$key"
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
