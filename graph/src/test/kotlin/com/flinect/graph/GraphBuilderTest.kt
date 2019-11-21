package com.flinect.graph

import com.flinect.scrap.common.JsonUtil
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Tag
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows

@Tag("unit")
class GraphBuilderTest {
    @Test
    fun basic() {
        val g = GraphBuilder.create {
            gate("action") {
                event("execute")
            }
            gate("text") {
                input("value", DataType.STRING)
                output("return-value", DataType.STRING)
            }
            gate("print") {
                command("print")
                input("content", DataType.STRING)
            }
            structure("user") {
                property("name", DataType.STRING)
            }
        }

        assertEquals(
            "{\"nodes\":{\"print\":{\"properties\":{\"print\":{\"id\":\"print\",\"direction\":\"IN\"},\"content\":{\"type\":\"STRING\",\"id\":\"content\",\"direction\":\"IN\"}},\"id\":\"print\"},\"action\":{\"properties\":{\"execute\":{\"id\":\"execute\",\"direction\":\"OUT\"}},\"id\":\"action\"},\"text\":{\"properties\":{\"return-value\":{\"type\":\"STRING\",\"id\":\"return-value\",\"direction\":\"OUT\"},\"value\":{\"type\":\"STRING\",\"id\":\"value\",\"direction\":\"IN\"}},\"id\":\"text\"},\"user\":{\"properties\":{\"name\":{\"type\":\"STRING\",\"id\":\"name\",\"direction\":\"OUT\"}},\"id\":\"user\"}}}",
            JsonUtil.encode(g)
        )
    }

    @Test
    fun errors() {
        assertThrows<IllegalArgumentException> {
            GraphBuilder.create {
                gate("a")
                gate("a")
            }
        }
        assertThrows<IllegalArgumentException> {
            GraphBuilder.create {
                structure("a")
                structure("a")
            }
        }
        assertThrows<IllegalArgumentException> {
            GraphBuilder.create {
                gate("a") {
                    event("a")
                    command("a")
                }
            }
        }
        assertThrows<IllegalArgumentException> {
            GraphBuilder.create {
                structure("a") {
                    property("a", DataType.INTEGER)
                    property("a", DataType.STRING)
                }
            }
        }
    }
}
