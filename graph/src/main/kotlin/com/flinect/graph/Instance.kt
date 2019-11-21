package com.flinect.graph

abstract class Instance(val key: String) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is Instance) return false

        if (key != other.key) return false

        return true
    }

    override fun hashCode(): Int {
        return key.hashCode()
    }
}
