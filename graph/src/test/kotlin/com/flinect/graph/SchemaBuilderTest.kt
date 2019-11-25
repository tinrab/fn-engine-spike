package com.flinect.graph

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Tag
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows

@Tag("unit")
class SchemaBuilderTest {
    @Test
    fun basic() {
        val s = SchemaBuilder.create {
            node("action") {
                event("execute")
            }
            node("text") {
                input("value", DataType.STRING)
                output("return-value", DataType.STRING)
            }
            node("print") {
                command("print")
                input("content", DataType.STRING)
            }
        }

//        assertEquals(
//            "{\"nodes\":{\"print\":{\"id\":\"print\",\"properties\":{\"print\":{\"id\":\"print\",\"direction\":\"IN\"},\"content\":{\"type\":\"STRING\",\"id\":\"content\",\"direction\":\"IN\"}}},\"action\":{\"id\":\"action\",\"properties\":{\"execute\":{\"id\":\"execute\",\"direction\":\"OUT\"}}},\"text\":{\"id\":\"text\",\"properties\":{\"return-value\":{\"type\":\"STRING\",\"id\":\"return-value\",\"direction\":\"OUT\"},\"value\":{\"type\":\"STRING\",\"id\":\"value\",\"direction\":\"IN\"}}}}}".hashCode(),
//            TestUtil.toJson(s).hashCode()
//        )
    }

    @Test
    fun errors() {
        assertThrows<IllegalArgumentException> {
            SchemaBuilder.create {
                node("a")
                node("a")
            }
        }
        assertThrows<IllegalArgumentException> {
            SchemaBuilder.create {
                node("a") {
                    event("a")
                    command("a")
                }
            }
        }
    }
}
