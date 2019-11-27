package com.flinect.engine

import com.flinect.graph.DataProperty
import com.flinect.graph.Node

data class InputReference(
    val node: Node,
    val property: DataProperty
)
