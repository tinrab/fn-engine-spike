package com.flinect.graph.flow

class Flow(
    val instances: Map<String, NodeInstance>,
    val edges: Map<String, Map<String, Edge>>
)
