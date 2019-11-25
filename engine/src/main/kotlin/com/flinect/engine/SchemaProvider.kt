package com.flinect.engine

import com.flinect.graph.DataType
import com.flinect.graph.Schema
import com.flinect.graph.SchemaBuilder

object SchemaProvider {
    fun get(): Schema {
        return SchemaBuilder.create {
            node("action") {
                command("trigger")
                event("triggered")
            }
            node("repeat") {
                command("start")
                event("executed")
                input("times", DataType.INTEGER)
            }
            node("printer") {
                command("print")
                input("content", DataType.INTEGER)
            }
            node("plus") {
                input("a", DataType.INTEGER)
                input("b", DataType.INTEGER)
                output("c", DataType.INTEGER)
            }
            node("minus") {
                input("a", DataType.INTEGER)
                input("b", DataType.INTEGER)
                output("c", DataType.INTEGER)
            }
            node("integer") {
                input("value", DataType.INTEGER)
                output("return-value", DataType.INTEGER)
            }
        }
    }
}
