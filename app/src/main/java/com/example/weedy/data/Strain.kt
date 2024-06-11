package com.example.weedy.data

data class Strain(
    val ocpc: String,
    val name: String,
    val qr: String,
    val url: String,
    val image: String,
    val seedCompany: SeedCompany,
    val genetics: Genetics?,
   // val lineage: Lineage?,
   // val children: List<String>?,
    val createdAt: String,
    val updatedAt: String
)

data class Lineage(
    val locations: Map<String, String>,
)

data class SeedCompany(
    val name: String,
    val ocpc: String
)

data class Genetics(
    val names: Any?,
    val ocpc: Any?
)
