package com.flinect.engine

import com.flinect.graph.value.Value
import kotlinx.coroutines.sync.Mutex

class State {
    private val state = HashMap<String, Value>()
    private val mutex = Mutex()
}
