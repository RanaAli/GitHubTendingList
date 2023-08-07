package com.rana.data.models.response

import com.google.gson.annotations.SerializedName

data class RepositoryResponse(
    val items: List<RepositoryItemResponse>? = null,
)

data class RepositoryItemResponse(
    val name: String? = null,
    val owner: OwnerResponse? = null,
    @SerializedName("stargazers_count")
    val score: String? = null,
    val url: String? = null,
    val description: String? = null,
    val language: String? = null
)

data class OwnerResponse(
    @SerializedName("avatar_url")
    val avatarUrl: String? = null,
)