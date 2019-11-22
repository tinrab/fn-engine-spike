package com.flinect.graph.flow

import com.flinect.graph.Gate
import com.flinect.graph.PropertyId

class GateInstance(
    val gate: Gate,
    key: String,
    val propertyValues: Map<PropertyId, PropertyValue>,
    val inputs: Map<PropertyId, Hook>,
    val outputs: Map<PropertyId, List<Hook>>
) : NodeInstance(gate.id, key)
