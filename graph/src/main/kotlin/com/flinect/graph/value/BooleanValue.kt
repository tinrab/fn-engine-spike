package com.flinect.graph.value

import com.flinect.graph.DataType

data class BooleanValue(val value: Boolean) : Value(DataType.BOOLEAN) {
    companion object {
        fun default(): BooleanValue {
            return BooleanValue(false)
        }
    }
}
