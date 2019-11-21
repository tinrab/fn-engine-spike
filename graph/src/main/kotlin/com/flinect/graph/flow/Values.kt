package com.flinect.graph.flow

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

data class StringValue(val value: String) : Value(DataType.STRING) {
    companion object {
        fun default(): StringValue {
            return StringValue("")
        }
    }
}

data class IntegerValue(val value: Long) : Value(DataType.INTEGER) {
    companion object {
        fun default(): IntegerValue {
            return IntegerValue(0)
        }
    }
}

data class FloatValue(val value: Double) : Value(DataType.FLOAT) {
    companion object {
        fun default(): FloatValue {
            return FloatValue(0.0)
        }
    }
}

data class BooleanValue(val value: Boolean) : Value(DataType.BOOLEAN) {
    companion object {
        fun default(): BooleanValue {
            return BooleanValue(false)
        }
    }
}
