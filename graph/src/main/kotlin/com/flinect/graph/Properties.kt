package com.flinect.graph

import com.flinect.graph.flow.Value

abstract class Property(val id: String, val direction: Direction)

abstract class ControlProperty(id: String, direction: Direction) : Property(id, direction)

class DataProperty(
    id: String,
    direction: Direction,
    val type: DataType,
    val defaultValue: Value? = null
) : Property(id, direction)

class EventProperty(id: String) : ControlProperty(id, Direction.OUT)

class CommandProperty(id: String) : ControlProperty(id, Direction.IN)
