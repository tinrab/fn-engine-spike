package com.flinect.graph.flow

class Edge(
    val source: Hook,
    val target: Hook
) {
    override fun toString(): String {
        return "$source>$target"
    }
}
