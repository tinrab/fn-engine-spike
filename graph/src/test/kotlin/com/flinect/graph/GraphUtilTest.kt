package com.flinect.graph

import org.junit.jupiter.api.Assertions.assertFalse
import org.junit.jupiter.api.Tag
import org.junit.jupiter.api.Test

@Tag("unit")
class GraphUtilTest {
    @Test
    fun isValidId() {
        assert(GraphUtil.isValidId("a"))
        assert(GraphUtil.isValidId("a-b"))
        assert(GraphUtil.isValidId("ab-cd"))
        assert(GraphUtil.isValidId("a.b"))
        assert(GraphUtil.isValidId("a-b.cd"))

        assertFalse(GraphUtil.isValidId("Ab"))
        assertFalse(GraphUtil.isValidId(""))
        assertFalse(GraphUtil.isValidId(" a "))
        assertFalse(GraphUtil.isValidId("a-"))
        assertFalse(GraphUtil.isValidId("-a"))
        assertFalse(GraphUtil.isValidId(".a"))
        assertFalse(GraphUtil.isValidId("a."))
        assertFalse(GraphUtil.isValidId("aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa"))
    }
}
