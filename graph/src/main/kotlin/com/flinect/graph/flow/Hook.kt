package com.flinect.graph.flow

class Hook(
    val nodeInstance: NodeInstance,
    val propertyId: String
) {
    val key: String
        get() = "${nodeInstance}#${propertyId}"

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is Hook) return false

        if (key != other.key) return false

        return true
    }

    override fun hashCode(): Int {
        return key.hashCode()
    }
}
