package com.flinect.graph

import com.flinect.graph.value.Value

typealias PropertyId = String

abstract class Property(val id: PropertyId, val direction: Direction)

abstract class ControlProperty(id: PropertyId, direction: Direction) : Property(id, direction)

class DataProperty(
    id: PropertyId,
    direction: Direction,
    val type: DataType,
    val defaultValue: Value? = null
) : Property(id, direction)

class EventProperty(id: PropertyId) : ControlProperty(id, Direction.OUT)

class CommandProperty(id: PropertyId) : ControlProperty(id, Direction.IN)
