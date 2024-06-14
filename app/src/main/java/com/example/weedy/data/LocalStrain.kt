package com.example.weedy.data

data class LocalStrain(
    val name: String,
    val img_url: String,
    val type: String,
    val thc_level: String,
    val most_common_terpene: String,
    val description: String,
    val effects: Map<String, String>
)