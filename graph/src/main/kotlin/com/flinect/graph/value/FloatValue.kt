package com.flinect.graph.value

import com.flinect.graph.DataType

data class FloatValue(val value: Double) : Value(DataType.FLOAT) {
    companion object {
        fun default(): FloatValue {
            return FloatValue(0.0)
        }
    }
}
