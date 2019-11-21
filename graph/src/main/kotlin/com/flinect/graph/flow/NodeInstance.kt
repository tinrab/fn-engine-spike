package com.flinect.graph.flow

abstract class NodeInstance(val id: String, val key: String) {
    override fun toString(): String {
        return "$id#$key"
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is NodeInstance) return false

        if (id != other.id) return false
        if (key != other.key) return false

        return true
    }

    override fun hashCode(): Int {
        var result = id.hashCode()
        result = 31 * result + key.hashCode()
        return result
    }
}
