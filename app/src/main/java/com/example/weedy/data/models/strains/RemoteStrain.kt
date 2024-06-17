package com.example.weedy.data.models.strains

data class RemoteStrain(
    val ocpc: String,
    val name: String,
    val seedCompany: SeedCompany,
    val genetics: Genetics?,
    val lineage: Any?,
    val children: Any?,
    val createdAt: String,
    val updatedAt: String
)


data class SeedCompany(
    val name: String?,
    val ocpc: String?,
)

data class Genetics(
    val names: Any?,
    val ocpc: Any?
)
