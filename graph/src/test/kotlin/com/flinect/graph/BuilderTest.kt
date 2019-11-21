package com.flinect.graph

import com.flinect.scrap.common.JsonUtil
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Tag
import org.junit.jupiter.api.Test

@Tag("unit")
class BuilderTest {
    @Test
    fun basic() {
        val g = GraphBuilder.create {
            node("action") {
                event("execute")
            }
            node("text") {
                input("value", DataType.STRING) {
                    connectable = false
                }
                output("return-value", DataType.STRING)
            }
            node("print") {
                command("print")
                input("content", DataType.STRING)
            }
        }

        assertEquals(
            "{\"nodes\":{\"print\":{\"id\":\"print\",\"properties\":{\"print\":{\"id\":\"print\",\"kind\":\"COMMAND\",\"connectable\":true},\"content\":{\"id\":\"content\",\"kind\":\"INPUT\",\"type\":\"STRING\",\"connectable\":true}}},\"action\":{\"id\":\"action\",\"properties\":{\"execute\":{\"id\":\"execute\",\"kind\":\"EVENT\",\"connectable\":true}}},\"text\":{\"id\":\"text\",\"properties\":{\"return-value\":{\"id\":\"return-value\",\"kind\":\"OUTPUT\",\"type\":\"STRING\",\"connectable\":true},\"value\":{\"id\":\"value\",\"kind\":\"INPUT\",\"type\":\"STRING\",\"connectable\":false}}}}}",
            JsonUtil.encode(g)
        )

        val f = FlowBuilder.with(g) {
            addNode("action", "a1")
            addNode("print", "p1")
            addNode("text", "t1") {
                setProperty("value", Value.createString("Hello"))
            }
        }
        println(JsonUtil.encode(f))
    }
}
