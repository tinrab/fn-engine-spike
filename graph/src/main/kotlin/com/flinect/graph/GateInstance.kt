package com.flinect.graph

class GateInstance(
    val gate: Gate,
    key: String,
    val values: Map<PropertyId, PropertyValue>,
    val inputs: Map<PropertyId, Hook>,
    val outputs: Map<PropertyId, List<Hook>>
) : NodeInstance(gate.id, key)
