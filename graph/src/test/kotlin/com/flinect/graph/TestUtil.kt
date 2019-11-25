package com.flinect.graph

import com.google.gson.GsonBuilder

object TestUtil {
    private val gson = GsonBuilder().create()

    fun toJson(value: Any): String {
        return gson.toJson(value)
    }
}
