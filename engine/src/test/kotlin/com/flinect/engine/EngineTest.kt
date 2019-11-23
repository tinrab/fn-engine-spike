package com.flinect.engine

import com.flinect.engine.actor.ActionActor
import com.flinect.engine.actor.PrinterActor
import com.flinect.graph.GraphBuilder
import com.flinect.graph.SchemaBuilder
import kotlinx.coroutines.runBlocking
import org.junit.jupiter.api.Tag
import org.junit.jupiter.api.Test

@Tag("unit")
class EngineTest {
    val schema = SchemaBuilder.create {
        gate(ActionActor.ID) {
            event(ActionActor.EVENT_ON_EXECUTE)
        }
        gate(PrinterActor.ID) {
            command(PrinterActor.COMMAND_PRINT)
        }
    }

    @Test
    fun basic() {
        val graph = GraphBuilder.create(schema) {
            val a1 = gate("action", "a1")
            val p1 = gate("printer", "p1")

            connect(
                property(a1, "on-execute"),
                property(p1, "print")
            )
        }

        val engine = Engine.create()
        engine.run()
        engine.execute(graph)
//        runBlocking {
//            while (true) {
//            }
//        }
    }
}
