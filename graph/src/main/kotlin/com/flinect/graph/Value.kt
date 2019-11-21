package com.flinect.graph

data class Value(
    val type: DataType,
    val value: Any
) {
    fun asString(): String {
        check(type == DataType.STRING)
        return value.toString()
    }

    fun asLong(): Long {
        check(type == DataType.INTEGER)
        return value as Long
    }

    companion object {
        fun createString(value: String) = Value(DataType.STRING, value)

        fun createInteger(value: Long) = Value(DataType.INTEGER, value)
    }
}
