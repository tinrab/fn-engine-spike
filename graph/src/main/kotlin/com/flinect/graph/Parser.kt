package com.flinect.graph

import com.flinect.graph.value.Value
import com.google.gson.GsonBuilder
import com.google.gson.JsonElement
import com.google.gson.JsonObject
import java.io.Reader
import java.io.StringReader

class Parser private constructor(
    private val schema: Schema
) {
    private val graphBuilder = GraphBuilder.create(schema)

    private fun buildNode(json: JsonObject) {
        val id = json["id"].asString
        val key = json["key"].asString

        val node = schema.nodes[id]
        requireNotNull(node) { "Node with id '$id' not found." }

        graphBuilder.node(id, key) {
            for (value in getValues(json)) {
                val property = property(value["id"].asString)
                require(property is DataProperty) { "Cannot assign a value to '${property.id}' property of gate '$id'." }
                property assign parseValue(property.type, value["value"])
            }
        }
    }

    private fun buildEdge(json: JsonObject) {
        require(json.has("source") && json.has("target")) { "Invalid edge object." }
        val sourceElement = json["source"]
        val targetElement = json["target"]
        require(sourceElement.isJsonObject && targetElement.isJsonObject) { "Invalid edge object." }
        val source = sourceElement.asJsonObject
        val target = targetElement.asJsonObject

        graphBuilder.connect(
            graphBuilder.property(source["nodeKey"].asString, source["propertyId"].asString),
            graphBuilder.property(target["nodeKey"].asString, target["propertyId"].asString)
        )
    }

    private fun getValues(json: JsonObject): Array<JsonObject> {
        if (!json.has("values")) {
            return emptyArray()
        }
        val values = json["values"]
        require(values.isJsonArray) { "Invalid values field." }
        return values.asJsonArray.map {
            require(it.isJsonObject) { "Invalid value object." }
            return@map it.asJsonObject
        }.toTypedArray()
    }

    private fun parseValue(type: DataType, json: JsonElement): Value {
        return when (type) {
            DataType.STRING -> Value.of(json.asString)
            DataType.INTEGER -> Value.of(json.asString.toLong())
            DataType.FLOAT -> Value.of(json.asDouble)
            DataType.BOOLEAN -> Value.of(json.asBoolean)
        }
    }

    private fun build(json: JsonObject): Graph {
        if (json.has("nodes")) {
            val nodesElement = json["nodes"]
            require(nodesElement.isJsonArray) { "Invalid nodes field." }
            for (node in nodesElement.asJsonArray) {
                require(node.isJsonObject) { "Invalid node object." }
                buildNode(node.asJsonObject)
            }
        }
        if (json.has("edges")) {
            val edgesElement = json["edges"]
            require(edgesElement.isJsonArray) { "Invalid edges field." }
            for (edge in edgesElement.asJsonArray) {
                require(edge.isJsonObject) { "Invalid edge object." }
                buildEdge(edge.asJsonObject)
            }
        }
        return graphBuilder.build()
    }

    companion object {
        private val gson = GsonBuilder().create()

        fun fromJson(schema: Schema, json: String): Graph {
            return fromJson(schema, StringReader(json))
        }

        fun fromJson(schema: Schema, reader: Reader): Graph {
            val jsonObject = gson.fromJson(reader, JsonObject::class.java).asJsonObject
            return Parser(schema).build(jsonObject)
        }
    }
}
