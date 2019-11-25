package com.flinect.graph

object GraphUtil {
    private val idRegex = Regex("^[a-z]+(?:[.-][a-z]+)*$")

    fun isValidId(id: String): Boolean {
        if (id.length > 32 || id.isEmpty()) {
            return false
        }
        return idRegex.matches(id)
    }
}
