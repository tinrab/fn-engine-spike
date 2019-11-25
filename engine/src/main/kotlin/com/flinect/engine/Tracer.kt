package com.flinect.engine

import com.flinect.graph.Node

class Tracer {
    private val logger = Logger(false)

    fun workerReceivedInstruction(instruction: Instruction) {
        logger.log("workerReceivedInstruction: instruction=$instruction")
    }

    fun instructionPassedToHandler(instruction: Instruction, node: Node) {
        logger.log("instructionPassedToHandler: instruction=$instruction, nodeId=${node.id}")
    }

    fun eventTriggered(event: EventReference) {
        logger.log("eventTriggered: nodeId=${event.node.id}, eventId=${event.property.id}")
    }

    fun broadcastInstruction(instruction: Instruction) {
        logger.log("broadcastInstruction: instruction=$instruction")
    }

    fun sentToWorker(instruction: Instruction) {
        logger.log("sentToWorker: instruction=$instruction")
    }

    inner class Logger(
        private val enabled: Boolean
    ) {
        fun log(value: String) {
            if (enabled) {
                println(value)
            }
        }
    }
}
