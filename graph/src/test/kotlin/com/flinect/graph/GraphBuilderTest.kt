package com.flinect.graph

import com.flinect.graph.value.Value
import com.flinect.scrap.common.JsonUtil
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Tag
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows

@Tag("unit")
class GraphBuilderTest {
    val graph = SchemaBuilder.create {
        gate("a") {
            event("event")
            command("command")
            input("input-string", DataType.STRING)
            output("output-string", DataType.STRING)
        }
        gate("b") {
            event("event")
            command("command")
            input("input-integer", DataType.INTEGER)
            output("output-integer", DataType.INTEGER)
        }
        structure("s") {
            property("string", DataType.STRING)
        }
    }

    @Test
    fun basic() {
        val f = GraphBuilder.create(graph) {
            val a1 = gate("a", "a1")
            val b1 = gate("b", "b1") {
                property("input-integer") assign Value.of(42)
            }
            val s1 = structure("s", "s1") {
                property("string") assign Value.of("abc")
            }
            property(a1, "event") connectTo property(b1, "command")
            property(s1, "string") connectTo property(a1, "input-string")
        }

        println(JsonUtil.encode(f))
        assertEquals(
            "{\"nodes\":{\"a1\":{\"gate\":{\"properties\":{\"output-string\":{\"type\":\"STRING\",\"id\":\"output-string\",\"direction\":\"OUT\"},\"input-string\":{\"type\":\"STRING\",\"id\":\"input-string\",\"direction\":\"IN\"},\"event\":{\"id\":\"event\",\"direction\":\"OUT\"},\"command\":{\"id\":\"command\",\"direction\":\"IN\"}},\"id\":\"a\"},\"values\":{},\"inputs\":{\"input-string\":{\"nodeInstance\":{\"structure\":{\"properties\":{\"string\":{\"type\":\"STRING\",\"id\":\"string\",\"direction\":\"OUT\"}},\"id\":\"s\"},\"values\":{\"string\":{\"property\":{\"type\":\"STRING\",\"id\":\"string\",\"direction\":\"OUT\"},\"value\":{\"value\":\"abc\",\"type\":\"STRING\"}}},\"id\":\"s\",\"key\":\"s1\"},\"property\":{\"type\":\"STRING\",\"id\":\"string\",\"direction\":\"OUT\"}}},\"outputs\":{\"event\":[{\"nodeInstance\":{\"gate\":{\"properties\":{\"output-integer\":{\"type\":\"INTEGER\",\"id\":\"output-integer\",\"direction\":\"OUT\"},\"input-integer\":{\"type\":\"INTEGER\",\"id\":\"input-integer\",\"direction\":\"IN\"},\"event\":{\"id\":\"event\",\"direction\":\"OUT\"},\"command\":{\"id\":\"command\",\"direction\":\"IN\"}},\"id\":\"b\"},\"values\":{\"input-integer\":{\"property\":{\"type\":\"INTEGER\",\"id\":\"input-integer\",\"direction\":\"IN\"},\"value\":{\"value\":42,\"type\":\"INTEGER\"}}},\"id\":\"b\",\"key\":\"b1\"},\"property\":{\"id\":\"command\",\"direction\":\"IN\"}}]},\"id\":\"a\",\"key\":\"a1\"},\"s1\":{\"structure\":{\"properties\":{\"string\":{\"type\":\"STRING\",\"id\":\"string\",\"direction\":\"OUT\"}},\"id\":\"s\"},\"values\":{\"string\":{\"property\":{\"type\":\"STRING\",\"id\":\"string\",\"direction\":\"OUT\"},\"value\":{\"value\":\"abc\",\"type\":\"STRING\"}}},\"inputs\":{},\"outputs\":{\"string\":[{\"nodeInstance\":{\"gate\":{\"properties\":{\"output-string\":{\"type\":\"STRING\",\"id\":\"output-string\",\"direction\":\"OUT\"},\"input-string\":{\"type\":\"STRING\",\"id\":\"input-string\",\"direction\":\"IN\"},\"event\":{\"id\":\"event\",\"direction\":\"OUT\"},\"command\":{\"id\":\"command\",\"direction\":\"IN\"}},\"id\":\"a\"},\"values\":{},\"id\":\"a\",\"key\":\"a1\"},\"property\":{\"type\":\"STRING\",\"id\":\"input-string\",\"direction\":\"IN\"}}]},\"id\":\"s\",\"key\":\"s1\"},\"b1\":{\"gate\":{\"properties\":{\"output-integer\":{\"type\":\"INTEGER\",\"id\":\"output-integer\",\"direction\":\"OUT\"},\"input-integer\":{\"type\":\"INTEGER\",\"id\":\"input-integer\",\"direction\":\"IN\"},\"event\":{\"id\":\"event\",\"direction\":\"OUT\"},\"command\":{\"id\":\"command\",\"direction\":\"IN\"}},\"id\":\"b\"},\"values\":{\"input-integer\":{\"property\":{\"type\":\"INTEGER\",\"id\":\"input-integer\",\"direction\":\"IN\"},\"value\":{\"value\":42,\"type\":\"INTEGER\"}}},\"inputs\":{\"command\":{\"nodeInstance\":{\"gate\":{\"properties\":{\"output-string\":{\"type\":\"STRING\",\"id\":\"output-string\",\"direction\":\"OUT\"},\"input-string\":{\"type\":\"STRING\",\"id\":\"input-string\",\"direction\":\"IN\"},\"event\":{\"id\":\"event\",\"direction\":\"OUT\"},\"command\":{\"id\":\"command\",\"direction\":\"IN\"}},\"id\":\"a\"},\"values\":{},\"id\":\"a\",\"key\":\"a1\"},\"property\":{\"id\":\"event\",\"direction\":\"OUT\"}}},\"outputs\":{},\"id\":\"b\",\"key\":\"b1\"}},\"edges\":{\"edgeMap\":{\"s#s1@string\\u003ea#a1@input-string\":{\"source\":{\"nodeInstance\":{\"structure\":{\"properties\":{\"string\":{\"type\":\"STRING\",\"id\":\"string\",\"direction\":\"OUT\"}},\"id\":\"s\"},\"values\":{\"string\":{\"property\":{\"type\":\"STRING\",\"id\":\"string\",\"direction\":\"OUT\"},\"value\":{\"value\":\"abc\",\"type\":\"STRING\"}}},\"id\":\"s\",\"key\":\"s1\"},\"property\":{\"type\":\"STRING\",\"id\":\"string\",\"direction\":\"OUT\"}},\"target\":{\"nodeInstance\":{\"gate\":{\"properties\":{\"output-string\":{\"type\":\"STRING\",\"id\":\"output-string\",\"direction\":\"OUT\"},\"input-string\":{\"type\":\"STRING\",\"id\":\"input-string\",\"direction\":\"IN\"},\"event\":{\"id\":\"event\",\"direction\":\"OUT\"},\"command\":{\"id\":\"command\",\"direction\":\"IN\"}},\"id\":\"a\"},\"values\":{},\"id\":\"a\",\"key\":\"a1\"},\"property\":{\"type\":\"STRING\",\"id\":\"input-string\",\"direction\":\"IN\"}}},\"a#a1@event\\u003eb#b1@command\":{\"source\":{\"nodeInstance\":{\"gate\":{\"properties\":{\"output-string\":{\"type\":\"STRING\",\"id\":\"output-string\",\"direction\":\"OUT\"},\"input-string\":{\"type\":\"STRING\",\"id\":\"input-string\",\"direction\":\"IN\"},\"event\":{\"id\":\"event\",\"direction\":\"OUT\"},\"command\":{\"id\":\"command\",\"direction\":\"IN\"}},\"id\":\"a\"},\"values\":{},\"id\":\"a\",\"key\":\"a1\"},\"property\":{\"id\":\"event\",\"direction\":\"OUT\"}},\"target\":{\"nodeInstance\":{\"gate\":{\"properties\":{\"output-integer\":{\"type\":\"INTEGER\",\"id\":\"output-integer\",\"direction\":\"OUT\"},\"input-integer\":{\"type\":\"INTEGER\",\"id\":\"input-integer\",\"direction\":\"IN\"},\"event\":{\"id\":\"event\",\"direction\":\"OUT\"},\"command\":{\"id\":\"command\",\"direction\":\"IN\"}},\"id\":\"b\"},\"values\":{\"input-integer\":{\"property\":{\"type\":\"INTEGER\",\"id\":\"input-integer\",\"direction\":\"IN\"},\"value\":{\"value\":42,\"type\":\"INTEGER\"}}},\"id\":\"b\",\"key\":\"b1\"},\"property\":{\"id\":\"command\",\"direction\":\"IN\"}}}},\"inputs\":{\"a#a1@input-string\":{\"source\":{\"nodeInstance\":{\"structure\":{\"properties\":{\"string\":{\"type\":\"STRING\",\"id\":\"string\",\"direction\":\"OUT\"}},\"id\":\"s\"},\"values\":{\"string\":{\"property\":{\"type\":\"STRING\",\"id\":\"string\",\"direction\":\"OUT\"},\"value\":{\"value\":\"abc\",\"type\":\"STRING\"}}},\"id\":\"s\",\"key\":\"s1\"},\"property\":{\"type\":\"STRING\",\"id\":\"string\",\"direction\":\"OUT\"}},\"target\":{\"nodeInstance\":{\"gate\":{\"properties\":{\"output-string\":{\"type\":\"STRING\",\"id\":\"output-string\",\"direction\":\"OUT\"},\"input-string\":{\"type\":\"STRING\",\"id\":\"input-string\",\"direction\":\"IN\"},\"event\":{\"id\":\"event\",\"direction\":\"OUT\"},\"command\":{\"id\":\"command\",\"direction\":\"IN\"}},\"id\":\"a\"},\"values\":{},\"id\":\"a\",\"key\":\"a1\"},\"property\":{\"type\":\"STRING\",\"id\":\"input-string\",\"direction\":\"IN\"}}},\"b#b1@command\":{\"source\":{\"nodeInstance\":{\"gate\":{\"properties\":{\"output-string\":{\"type\":\"STRING\",\"id\":\"output-string\",\"direction\":\"OUT\"},\"input-string\":{\"type\":\"STRING\",\"id\":\"input-string\",\"direction\":\"IN\"},\"event\":{\"id\":\"event\",\"direction\":\"OUT\"},\"command\":{\"id\":\"command\",\"direction\":\"IN\"}},\"id\":\"a\"},\"values\":{},\"id\":\"a\",\"key\":\"a1\"},\"property\":{\"id\":\"event\",\"direction\":\"OUT\"}},\"target\":{\"nodeInstance\":{\"gate\":{\"properties\":{\"output-integer\":{\"type\":\"INTEGER\",\"id\":\"output-integer\",\"direction\":\"OUT\"},\"input-integer\":{\"type\":\"INTEGER\",\"id\":\"input-integer\",\"direction\":\"IN\"},\"event\":{\"id\":\"event\",\"direction\":\"OUT\"},\"command\":{\"id\":\"command\",\"direction\":\"IN\"}},\"id\":\"b\"},\"values\":{\"input-integer\":{\"property\":{\"type\":\"INTEGER\",\"id\":\"input-integer\",\"direction\":\"IN\"},\"value\":{\"value\":42,\"type\":\"INTEGER\"}}},\"id\":\"b\",\"key\":\"b1\"},\"property\":{\"id\":\"command\",\"direction\":\"IN\"}}}},\"outputs\":{\"s#s1@string\":[{\"source\":{\"nodeInstance\":{\"structure\":{\"properties\":{\"string\":{\"type\":\"STRING\",\"id\":\"string\",\"direction\":\"OUT\"}},\"id\":\"s\"},\"values\":{\"string\":{\"property\":{\"type\":\"STRING\",\"id\":\"string\",\"direction\":\"OUT\"},\"value\":{\"value\":\"abc\",\"type\":\"STRING\"}}},\"id\":\"s\",\"key\":\"s1\"},\"property\":{\"type\":\"STRING\",\"id\":\"string\",\"direction\":\"OUT\"}},\"target\":{\"nodeInstance\":{\"gate\":{\"properties\":{\"output-string\":{\"type\":\"STRING\",\"id\":\"output-string\",\"direction\":\"OUT\"},\"input-string\":{\"type\":\"STRING\",\"id\":\"input-string\",\"direction\":\"IN\"},\"event\":{\"id\":\"event\",\"direction\":\"OUT\"},\"command\":{\"id\":\"command\",\"direction\":\"IN\"}},\"id\":\"a\"},\"values\":{},\"id\":\"a\",\"key\":\"a1\"},\"property\":{\"type\":\"STRING\",\"id\":\"input-string\",\"direction\":\"IN\"}}}],\"a#a1@event\":[{\"source\":{\"nodeInstance\":{\"gate\":{\"properties\":{\"output-string\":{\"type\":\"STRING\",\"id\":\"output-string\",\"direction\":\"OUT\"},\"input-string\":{\"type\":\"STRING\",\"id\":\"input-string\",\"direction\":\"IN\"},\"event\":{\"id\":\"event\",\"direction\":\"OUT\"},\"command\":{\"id\":\"command\",\"direction\":\"IN\"}},\"id\":\"a\"},\"values\":{},\"id\":\"a\",\"key\":\"a1\"},\"property\":{\"id\":\"event\",\"direction\":\"OUT\"}},\"target\":{\"nodeInstance\":{\"gate\":{\"properties\":{\"output-integer\":{\"type\":\"INTEGER\",\"id\":\"output-integer\",\"direction\":\"OUT\"},\"input-integer\":{\"type\":\"INTEGER\",\"id\":\"input-integer\",\"direction\":\"IN\"},\"event\":{\"id\":\"event\",\"direction\":\"OUT\"},\"command\":{\"id\":\"command\",\"direction\":\"IN\"}},\"id\":\"b\"},\"values\":{\"input-integer\":{\"property\":{\"type\":\"INTEGER\",\"id\":\"input-integer\",\"direction\":\"IN\"},\"value\":{\"value\":42,\"type\":\"INTEGER\"}}},\"id\":\"b\",\"key\":\"b1\"},\"property\":{\"id\":\"command\",\"direction\":\"IN\"}}}]}}}".hashCode(),
            JsonUtil.encode(f).hashCode()
        )
    }

    @Test
    fun errors() {
        assertThrows<IllegalArgumentException> {
            GraphBuilder.create(graph) {
                gate("a", "a1")
                gate("a", "a1")
            }
        }
        assertThrows<IllegalArgumentException> {
            GraphBuilder.create(graph) {
                gate("asdf", "a1")
            }
        }
        assertThrows<IllegalArgumentException> {
            GraphBuilder.create(graph) {
                gate("a", "a1") {
                    property("asdf") assign Value.of("Hello")
                }
            }
        }
        assertThrows<IllegalArgumentException> {
            GraphBuilder.create(graph) {
                gate("a", "a1") {
                    property("command") assign Value.of("Hello")
                }
            }
        }
        assertThrows<IllegalArgumentException> {
            GraphBuilder.create(graph) {
                val a1 = gate("a", "a1")
                property(a1, "asdf")
            }
        }
        assertThrows<IllegalArgumentException> {
            GraphBuilder.create(graph) {
                gate("s", "s1")
            }
        }

        GraphBuilder.create(graph) {
            val a1 = gate("a", "a1")
            val b1 = gate("b", "b1")

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
