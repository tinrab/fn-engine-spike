package com.flinect.graph.value

import com.flinect.graph.DataType

data class StringValue(val value: String) : Value(DataType.STRING) {
    companion object {
        fun default(): StringValue {
            return StringValue("")
        }
    }
}
