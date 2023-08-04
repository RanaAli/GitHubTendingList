package com.rana.data.models.response

data class RepositoryResponse(
    val items: List<RepositoryItemResponse>? = null,
)

data class RepositoryItemResponse(
    val name: String? = null,
    val owner: OwnerResponse? = null,
    val score: Double? = null,
    val url: String? = null,
    val description: String? = null,
    val language: String? = null
)

data class OwnerResponse(
    val avatarUrl: String? = null,
)