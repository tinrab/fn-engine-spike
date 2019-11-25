package com.flinect.engine

import com.flinect.graph.PropertyId

data class Instruction(
    val context: Context,
    val commandId: PropertyId
) {
    override fun toString(): String {
        return "Instruction(node=${context.currentNode}, commandId=$commandId)"
    }
}
