package com.flinect.graph.value

import com.flinect.graph.DataType

data class IntegerValue(val value: Long) : Value(DataType.INTEGER) {
    companion object {
        fun default(): IntegerValue {
            return IntegerValue(0)
        }
    }
}
