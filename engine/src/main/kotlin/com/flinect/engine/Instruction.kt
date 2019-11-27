package com.flinect.engine

import com.flinect.graph.PropertyId

class Instruction(
    context: Context,
    val commandId: PropertyId
) : Message(context) {
    override fun toString(): String {
        return "Instruction(node=${context.currentNode}, commandId=$commandId)"
    }
}
