package com.flinect.graph

abstract class Node(
    val id: String
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
