package com.flinect.graph

class Hook(
    val nodeInstance: NodeInstance,
    val property: Property
) {
    val key: String
        get() = "${nodeInstance}@${property.id}"

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
