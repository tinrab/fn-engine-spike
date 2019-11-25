package com.flinect.engine

import com.flinect.engine.config.EngineConfig
import com.flinect.graph.GraphBuilder
import com.flinect.graph.value.Value
import org.junit.jupiter.api.Tag
import org.junit.jupiter.api.Test

@Tag("unit")
class EngineTest {
    @Test
    fun basic() {
        val schema = SchemaProvider.get()

        val graph = GraphBuilder.create(schema) {
            val a1 = node("action", "a1")
            val r1 = node("repeat", "r1") {
                property("times") assign Value.of(3)
            }
            val p1 = node("printer", "p1")
            val minus = node("minus", "minus")
            val plus = node("plus", "plus")
            val six = node("integer", "six") {
                property("value") assign Value.of(6)
            }
            val four = node("integer", "four") {
                property("value") assign Value.of(4)
            }
            val three = node("integer", "three") {
                property("value") assign Value.of(3)
            }

            property(a1, "triggered") connectTo property(r1, "start")
            property(r1, "executed") connectTo property(p1, "print")

            property(six, "return-value") connectTo property(plus, "a")
            property(four, "return-value") connectTo property(plus, "b")
            property(plus, "c") connectTo property(minus, "a")
            property(three, "return-value") connectTo property(minus, "b")
            property(minus, "c") connectTo property(p1, "content")
        }

        val engine = Engine(EngineConfig(), schema)
        engine.run()
        engine.execute(graph)
    }
}
