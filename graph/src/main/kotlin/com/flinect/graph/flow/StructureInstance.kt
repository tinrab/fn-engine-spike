package com.flinect.graph.flow

import com.flinect.graph.PropertyId
import com.flinect.graph.Structure

class StructureInstance(
    val structure: Structure,
    key: String,
    val propertyValues: Map<PropertyId, PropertyValue>,
    val inputs: Map<PropertyId, Hook>,
    val outputs: Map<PropertyId, List<Hook>>
) : NodeInstance(structure.id, key)
