package com.flinect.engine

import com.flinect.graph.CommandProperty
import com.flinect.graph.Node

data class CommandReference(
    val node: Node,
    val property: CommandProperty
)
