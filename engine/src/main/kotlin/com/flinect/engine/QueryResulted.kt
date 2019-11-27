package com.flinect.engine

import com.flinect.graph.value.Value

class QueryResulted(
    context: Context,
    val value: Value,
    val senderWorkerId: Long
) : Message(context) {
    override fun toString(): String {
        return "QueryResulted(node=${context.currentNode}, value=$value, senderWorkerId=$senderWorkerId)"
    }
}
