package com.flinect.engine

import com.flinect.graph.NodeInstance
import com.flinect.graph.PropertyId

class Query(
    context: Context,
    val propertyId: PropertyId,
    val senderNode: NodeInstance,
    val senderWorkerId: Long
) : Message(context) {
    override fun toString(): String {
        return "Query(node=${context.currentNode}, propertyId=$propertyId, senderWorkerId=$senderWorkerId)"
    }
}
