package com.flinect.graph.value

import com.flinect.graph.DataType

abstract class Value(
    val type: DataType
) {
    companion object {
        fun of(value: String): StringValue {
            return StringValue(value)
        }

        fun of(value: Long): IntegerValue {
            return IntegerValue(value)
        }

        fun of(value: Double): FloatValue {
            return FloatValue(value)
        }

        fun of(value: Boolean): BooleanValue {
            return BooleanValue(value)
        }

        fun defaultValueForType(type: DataType): Value {
            return when (type) {
                DataType.STRING -> StringValue.default()
                DataType.INTEGER -> IntegerValue.default()
                DataType.FLOAT -> FloatValue.default()
                DataType.BOOLEAN -> BooleanValue.default()
            }
        }
    }
}
