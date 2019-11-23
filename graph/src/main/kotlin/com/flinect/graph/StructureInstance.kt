package com.flinect.graph

class StructureInstance(
    val structure: Structure,
    key: String,
    val values: Map<PropertyId, PropertyValue>,
    val inputs: Map<PropertyId, Hook>,
    val outputs: Map<PropertyId, List<Hook>>
) : NodeInstance(structure.id, key)
