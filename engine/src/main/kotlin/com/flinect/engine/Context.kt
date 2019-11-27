package com.flinect.engine

import com.flinect.graph.Graph
import com.flinect.graph.NodeInstance

data class Context(
    val graph: Graph,
    val currentNode: NodeInstance
)
