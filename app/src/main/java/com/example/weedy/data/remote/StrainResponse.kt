package com.example.weedy.data.remote

import com.example.weedy.data.Strain

data class StrainResponse(
    val data: List<Strain>,
    val meta: Meta
)

data class Meta(
    val pagination: Pagination
)

data class Pagination(
    val total: Int,
    val count: Int,
    val per_page: Int,
    val current_page: Int,
    val total_pages: Int,
    val links: Links
)

data class Links(
    val previous: String?,
    val next: String?
)