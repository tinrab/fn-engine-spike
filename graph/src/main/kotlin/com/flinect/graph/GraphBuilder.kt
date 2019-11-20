package com.flinect.graph

class GraphBuilder {

    fun build(): Graph {
        return Graph()
    }

    companion object {
        fun ofJson(json: String): GraphBuilder {
            return GraphBuilder()
        }
    }
}
