package com.flinect.graph

import com.flinect.graph.value.Value
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Tag
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows

@Tag("unit")
class GraphBuilderTest {
    val schema = SchemaBuilder.create {
        node("a") {
            event("event")
            command("command")
            input("input-string", DataType.STRING)
            output("output-string", DataType.STRING)
        }
        node("b") {
            event("event")
            command("command")
            input("input-integer", DataType.INTEGER)
            output("output-integer", DataType.INTEGER)
        }
        node("c") {
            event("event")
            command("command")
            input("input-integer", DataType.INTEGER)
            output("output-integer", DataType.INTEGER)
        }
    }

    @Test
    fun basic() {
        val graph = GraphBuilder.create(schema) {
            val a1 = node("a", "a1")
            val b1 = node("b", "b1") {
                property("input-integer") assign Value.of(42)
            }
            val c1 =node("c", "c1")

            property(a1, "event") connectTo property(b1, "command")
            property(b1, "event") connectTo property(c1, "command")
        }

        val out = graph.nodes["b1"]?.getOutputs("event")?.get(0)!!
        println(out.nodeInstance.key)

        assertEquals(
            "{\"nodes\":{\"a1\":{\"node\":{\"id\":\"a\",\"properties\":{\"output-string\":{\"type\":\"STRING\",\"id\":\"output-string\",\"direction\":\"OUT\"},\"input-string\":{\"type\":\"STRING\",\"id\":\"input-string\",\"direction\":\"IN\"},\"event\":{\"id\":\"event\",\"direction\":\"OUT\"},\"command\":{\"id\":\"command\",\"direction\":\"IN\"}}},\"key\":\"a1\",\"values\":{},\"inputs\":{},\"outputs\":{\"event\":[{\"nodeInstance\":{\"node\":{\"id\":\"b\",\"properties\":{\"output-integer\":{\"type\":\"INTEGER\",\"id\":\"output-integer\",\"direction\":\"OUT\"},\"input-integer\":{\"type\":\"INTEGER\",\"id\":\"input-integer\",\"direction\":\"IN\"},\"event\":{\"id\":\"event\",\"direction\":\"OUT\"},\"command\":{\"id\":\"command\",\"direction\":\"IN\"}}},\"key\":\"b1\",\"values\":{\"input-integer\":{\"property\":{\"type\":\"INTEGER\",\"id\":\"input-integer\",\"direction\":\"IN\"},\"value\":{\"value\":42,\"type\":\"INTEGER\"}}},\"inputs\":{},\"outputs\":{}},\"property\":{\"id\":\"command\",\"direction\":\"IN\"}}]}},\"b1\":{\"node\":{\"id\":\"b\",\"properties\":{\"output-integer\":{\"type\":\"INTEGER\",\"id\":\"output-integer\",\"direction\":\"OUT\"},\"input-integer\":{\"type\":\"INTEGER\",\"id\":\"input-integer\",\"direction\":\"IN\"},\"event\":{\"id\":\"event\",\"direction\":\"OUT\"},\"command\":{\"id\":\"command\",\"direction\":\"IN\"}}},\"key\":\"b1\",\"values\":{\"input-integer\":{\"property\":{\"type\":\"INTEGER\",\"id\":\"input-integer\",\"direction\":\"IN\"},\"value\":{\"value\":42,\"type\":\"INTEGER\"}}},\"inputs\":{\"command\":{\"nodeInstance\":{\"node\":{\"id\":\"a\",\"properties\":{\"output-string\":{\"type\":\"STRING\",\"id\":\"output-string\",\"direction\":\"OUT\"},\"input-string\":{\"type\":\"STRING\",\"id\":\"input-string\",\"direction\":\"IN\"},\"event\":{\"id\":\"event\",\"direction\":\"OUT\"},\"command\":{\"id\":\"command\",\"direction\":\"IN\"}}},\"key\":\"a1\",\"values\":{},\"inputs\":{},\"outputs\":{}},\"property\":{\"id\":\"event\",\"direction\":\"OUT\"}}},\"outputs\":{}}},\"edges\":{\"edgeMap\":{\"a#a1@event\\u003eb#b1@command\":{\"source\":{\"nodeInstance\":{\"node\":{\"id\":\"a\",\"properties\":{\"output-string\":{\"type\":\"STRING\",\"id\":\"output-string\",\"direction\":\"OUT\"},\"input-string\":{\"type\":\"STRING\",\"id\":\"input-string\",\"direction\":\"IN\"},\"event\":{\"id\":\"event\",\"direction\":\"OUT\"},\"command\":{\"id\":\"command\",\"direction\":\"IN\"}}},\"key\":\"a1\",\"values\":{},\"inputs\":{},\"outputs\":{}},\"property\":{\"id\":\"event\",\"direction\":\"OUT\"}},\"target\":{\"nodeInstance\":{\"node\":{\"id\":\"b\",\"properties\":{\"output-integer\":{\"type\":\"INTEGER\",\"id\":\"output-integer\",\"direction\":\"OUT\"},\"input-integer\":{\"type\":\"INTEGER\",\"id\":\"input-integer\",\"direction\":\"IN\"},\"event\":{\"id\":\"event\",\"direction\":\"OUT\"},\"command\":{\"id\":\"command\",\"direction\":\"IN\"}}},\"key\":\"b1\",\"values\":{\"input-integer\":{\"property\":{\"type\":\"INTEGER\",\"id\":\"input-integer\",\"direction\":\"IN\"},\"value\":{\"value\":42,\"type\":\"INTEGER\"}}},\"inputs\":{},\"outputs\":{}},\"property\":{\"id\":\"command\",\"direction\":\"IN\"}}}},\"inputs\":{\"b#b1@command\":{\"source\":{\"nodeInstance\":{\"node\":{\"id\":\"a\",\"properties\":{\"output-string\":{\"type\":\"STRING\",\"id\":\"output-string\",\"direction\":\"OUT\"},\"input-string\":{\"type\":\"STRING\",\"id\":\"input-string\",\"direction\":\"IN\"},\"event\":{\"id\":\"event\",\"direction\":\"OUT\"},\"command\":{\"id\":\"command\",\"direction\":\"IN\"}}},\"key\":\"a1\",\"values\":{},\"inputs\":{},\"outputs\":{}},\"property\":{\"id\":\"event\",\"direction\":\"OUT\"}},\"target\":{\"nodeInstance\":{\"node\":{\"id\":\"b\",\"properties\":{\"output-integer\":{\"type\":\"INTEGER\",\"id\":\"output-integer\",\"direction\":\"OUT\"},\"input-integer\":{\"type\":\"INTEGER\",\"id\":\"input-integer\",\"direction\":\"IN\"},\"event\":{\"id\":\"event\",\"direction\":\"OUT\"},\"command\":{\"id\":\"command\",\"direction\":\"IN\"}}},\"key\":\"b1\",\"values\":{\"input-integer\":{\"property\":{\"type\":\"INTEGER\",\"id\":\"input-integer\",\"direction\":\"IN\"},\"value\":{\"value\":42,\"type\":\"INTEGER\"}}},\"inputs\":{},\"outputs\":{}},\"property\":{\"id\":\"command\",\"direction\":\"IN\"}}}},\"outputs\":{\"a#a1@event\":[{\"source\":{\"nodeInstance\":{\"node\":{\"id\":\"a\",\"properties\":{\"output-string\":{\"type\":\"STRING\",\"id\":\"output-string\",\"direction\":\"OUT\"},\"input-string\":{\"type\":\"STRING\",\"id\":\"input-string\",\"direction\":\"IN\"},\"event\":{\"id\":\"event\",\"direction\":\"OUT\"},\"command\":{\"id\":\"command\",\"direction\":\"IN\"}}},\"key\":\"a1\",\"values\":{},\"inputs\":{},\"outputs\":{}},\"property\":{\"id\":\"event\",\"direction\":\"OUT\"}},\"target\":{\"nodeInstance\":{\"node\":{\"id\":\"b\",\"properties\":{\"output-integer\":{\"type\":\"INTEGER\",\"id\":\"output-integer\",\"direction\":\"OUT\"},\"input-integer\":{\"type\":\"INTEGER\",\"id\":\"input-integer\",\"direction\":\"IN\"},\"event\":{\"id\":\"event\",\"direction\":\"OUT\"},\"command\":{\"id\":\"command\",\"direction\":\"IN\"}}},\"key\":\"b1\",\"values\":{\"input-integer\":{\"property\":{\"type\":\"INTEGER\",\"id\":\"input-integer\",\"direction\":\"IN\"},\"value\":{\"value\":42,\"type\":\"INTEGER\"}}},\"inputs\":{},\"outputs\":{}},\"property\":{\"id\":\"command\",\"direction\":\"IN\"}}}]}}}".hashCode(),
            TestUtil.toJson(graph).hashCode()
        )
    }

    @Test
    fun errors() {
        assertThrows<IllegalArgumentException> {
            GraphBuilder.create(schema) {
                node("a", "a1")
                node("a", "a1")
            }
        }
        assertThrows<IllegalArgumentException> {
            GraphBuilder.create(schema) {
                node("asdf", "a1")
            }
        }
        assertThrows<IllegalArgumentException> {
            GraphBuilder.create(schema) {
                node("a", "a1") {
                    property("asdf") assign Value.of("Hello")
                }
            }
        }
        assertThrows<IllegalArgumentException> {
            GraphBuilder.create(schema) {
                node("a", "a1") {
                    property("command") assign Value.of("Hello")
                }
            }
        }
        assertThrows<IllegalArgumentException> {
            GraphBuilder.create(schema) {
                val a1 = node("a", "a1")
                property(a1, "asdf")
            }
        }
        assertThrows<IllegalArgumentException> {
            GraphBuilder.create(schema) {
                node("s", "s1")
            }
        }

        GraphBuilder.create(schema) {
            val a1 = node("a", "a1")
            val b1 = node("b", "b1")

            property(a1, "event") connectTo property(b1, "command")

            val cases = arrayOf(
                { property(a1, "input-string") connectTo property(b1, "event") },
                { property(a1, "command") connectTo property(b1, "event") },
                { property(a1, "output-string") connectTo property(b1, "command") },
                { property(a1, "output-string") connectTo property(b1, "output-integer") },
                { property(a1, "output-string") connectTo property(b1, "event") },
                { property(a1, "event") connectTo property(b1, "output-integer") },
                { property(a1, "event") connectTo property(b1, "input-integer") },
                { property(a1, "output-string") connectTo property(b1, "command") },
                { property(a1, "input-string") connectTo property(b1, "command") },
                { property(a1, "output-string") connectTo property(b1, "input-integer") },
                { property(a1, "output-string") connectTo property(a1, "input-string") },
                { property(a1, "event") connectTo property(b1, "command") }
            )
            for (i in cases.indices) {
                assertThrows<IllegalArgumentException>("Case#$i") { cases[i]() }
            }
        }
    }
}
