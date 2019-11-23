package com.flinect.graph

class EdgeMap {
    private val edgeMap = HashMap<String, Edge>()
    private val inputs = HashMap<String, Edge>()
    private val outputs = HashMap<String, MutableList<Edge>>()

    val edges: Collection<Edge>
        get() = edgeMap.values

    internal fun put(e: Edge) {
        edgeMap[getKey(e)] = e
        inputs[e.target.key] = e
        if (!outputs.containsKey(e.source.key)) {
            outputs[e.source.key] = arrayListOf()
        }
        outputs[e.source.key]?.add(e)
    }

    operator fun get(source: Hook, target: Hook): Edge? {
        return edgeMap[getKey(source, target)]
    }

    fun getInput(target: Hook): Edge? {
        return inputs[target.key]
    }

    fun getOutputs(source: Hook): List<Edge> {
        return outputs[source.key] ?: emptyList()
    }

    fun contains(e: Edge): Boolean {
        return contains(e.source, e.target)
    }

    fun contains(source: Hook, target: Hook): Boolean {
        return get(source, target) != null
    }

    private fun getKey(e: Edge): String {
        return getKey(e.source, e.target)
    }

    private fun getKey(source: Hook, target: Hook): String {
        return "${source.key}>${target.key}"
    }
}
