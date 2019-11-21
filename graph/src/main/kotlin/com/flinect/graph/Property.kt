package com.flinect.graph

data class Property(
    val id: String,
    val kind: Kind,
    val type: DataType?,
    val connectable: Boolean
) {
    enum class Kind {
        OUTPUT,
        INPUT,
        EVENT,
        COMMAND
    }

    fun isData(): Boolean {
        return kind == Kind.OUTPUT || kind == Kind.INPUT
    }

    fun isControl(): Boolean {
        return kind == Kind.EVENT || kind == Kind.COMMAND
    }
}
