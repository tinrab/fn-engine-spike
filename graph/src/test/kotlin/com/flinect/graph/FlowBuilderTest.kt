package com.flinect.graph

import com.flinect.graph.flow.FlowBuilder
import com.flinect.graph.flow.Value
import com.flinect.scrap.common.JsonUtil
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Tag
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows

@Tag("unit")
class FlowBuilderTest {
    val graph = GraphBuilder.create {
        gate("a") {
            event("event")
            command("command")
            input("inputString", DataType.STRING)
            output("outputString", DataType.STRING)
        }
        gate("b") {
            event("event")
            command("command")
            input("inputInteger", DataType.STRING)
            output("outputInteger", DataType.STRING)
        }
        structure("s") {
            property("string", DataType.STRING)
        }
    }

    @Test
    fun basic() {
        val f = FlowBuilder.create(graph) {
            val a1 = gate("a", "a1")
            val b1 = gate("b", "b1") {
                property("inputInteger") assign Value.of(42)
            }
            val s1 = structure("s", "s1") {
                property("string") assign Value.of("abc")
            }
            property(a1, "event") connect property(b1, "command")
            property(s1, "string") connect property(a1, "inputString")
        }

        assertEquals(
            "{\"instances\":{\"a1\":{\"propertyValues\":{},\"id\":\"a\",\"key\":\"a1\"},\"s1\":{\"propertyValues\":{\"string\":{\"propertyId\":\"string\",\"value\":{\"value\":\"abc\",\"type\":\"STRING\"}}},\"id\":\"s\",\"key\":\"s1\"},\"b1\":{\"propertyValues\":{\"inputInteger\":{\"propertyId\":\"inputInteger\",\"value\":{\"value\":42,\"type\":\"INTEGER\"}}},\"id\":\"b\",\"key\":\"b1\"}},\"edges\":{\"a#a1#event\":{\"b#b1#command\":{\"source\":{\"nodeInstance\":{\"propertyValues\":{},\"id\":\"a\",\"key\":\"a1\"},\"propertyId\":\"event\"},\"target\":{\"nodeInstance\":{\"propertyValues\":{\"inputInteger\":{\"propertyId\":\"inputInteger\",\"value\":{\"value\":42,\"type\":\"INTEGER\"}}},\"id\":\"b\",\"key\":\"b1\"},\"propertyId\":\"command\"}}},\"s#s1#string\":{\"a#a1#inputString\":{\"source\":{\"nodeInstance\":{\"propertyValues\":{\"string\":{\"propertyId\":\"string\",\"value\":{\"value\":\"abc\",\"type\":\"STRING\"}}},\"id\":\"s\",\"key\":\"s1\"},\"propertyId\":\"string\"},\"target\":{\"nodeInstance\":{\"propertyValues\":{},\"id\":\"a\",\"key\":\"a1\"},\"propertyId\":\"inputString\"}}}}}",
            JsonUtil.encode(f)
        )
    }

    @Test
    fun errors() {
        assertThrows<IllegalArgumentException> {
            FlowBuilder.create(graph) {
                gate("a", "a1")
                gate("a", "a1")
            }
        }
        assertThrows<IllegalArgumentException> {
            FlowBuilder.create(graph) {
                gate("asdf", "a1")
            }
        }
        assertThrows<IllegalArgumentException> {
            FlowBuilder.create(graph) {
                gate("a", "a1") {
                    property("asdf") assign Value.of("Hello")
                }
            }
        }
        assertThrows<IllegalArgumentException> {
            FlowBuilder.create(graph) {
                gate("a", "a1") {
                    property("command") assign Value.of("Hello")
                }
            }
        }
        assertThrows<IllegalArgumentException> {
            FlowBuilder.create(graph) {
                val a1 = gate("a", "a1")
                property(a1, "asdf")
            }
        }

        FlowBuilder.create(graph) {
            val a1 = gate("a", "a1")
            val b1 = gate("b", "b1")

            property(a1, "event") connect property(b1, "command")

            val cases = arrayOf(
                { property(a1, "inputString") connect property(b1, "event") },
                { property(a1, "command") connect property(b1, "event") },
                { property(a1, "outputString") connect property(b1, "command") },
                { property(a1, "outputString") connect property(b1, "outputInteger") },
                { property(a1, "outputString") connect property(b1, "event") },
                { property(a1, "event") connect property(b1, "outputInteger") },
                { property(a1, "event") connect property(b1, "inputInteger") },
                { property(a1, "outputString") connect property(b1, "command") },
                { property(a1, "inputString") connect property(b1, "command") },
                { property(a1, "outputString") connect property(b1, "inputInteger") },
                { property(a1, "outputString") connect property(a1, "inputString") },
                { property(a1, "event") connect property(b1, "command") }
            )
            for (c in cases) {
                assertThrows<IllegalArgumentException> { c() }
            }
        }
    }
}
