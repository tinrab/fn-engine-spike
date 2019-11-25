package com.flinect.engine

import com.flinect.graph.EventProperty
import com.flinect.graph.Node

data class EventReference(
    val node: Node,
    val property: EventProperty
)
