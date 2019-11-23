package com.flinect.engine

import com.flinect.engine.actor.ActionActor
import com.flinect.graph.Graph
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.launch

class Engine {
    private val outbox = Channel<Message>()

    fun run() {
        val n = 1
        repeat(n) {
            GlobalScope.launch {
                val a = ActionActor(this)
                val actorInboxChannel = Channel<Message>()
                launch {
                    for (message in outbox) {
                        actorInboxChannel.send(message)
                    }
                }
                a.run(actorInboxChannel)
            }
        }
    }

    fun execute(graph: Graph) {
//        runBlocking {
//            delay(1000)
//            while (true) {
//                outbox.send(PingMessage())
//                delay(300)
//            }
//        }

    }

    companion object {
        fun create(): Engine {
            return Engine()
        }
    }
}
