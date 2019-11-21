package com.flinect.graph

@GraphDsl
class FlowBuilder private constructor(
    val g: Graph
) {
    private val instances = HashMap<String, Instance>()

    fun gate(id: String, key: String) {
        require(!instances.containsKey(key)) { "Key '$key' is taken." }
    }

    fun build(): Flow {
        return Flow(instances)
    }

    companion object {
        fun create(g: Graph, f: FlowBuilder.() -> Unit): Flow {
            return FlowBuilder(g).apply(f).build()
        }
    }
}
