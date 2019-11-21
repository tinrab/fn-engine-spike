package com.flinect.graph

class Graph(
    val nodes: Map<String, Node>
) {
    val gates: Map<String, Gate>
        get() = nodes.filterValues { node -> node is Gate }
            .mapValues { entry -> entry.value as Gate }

    val structures: Map<String, Structure>
        get() = nodes.filterValues { node -> node is Structure }
            .mapValues { entry -> entry.value as Structure }
}
