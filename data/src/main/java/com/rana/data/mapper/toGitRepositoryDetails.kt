package com.rana.data.mapper

import com.rana.data.models.response.RepositoryItemResponse
import com.rana.domain.entity.RepositoryItemEntity

fun List<RepositoryItemResponse>.toRepositoryItemEntity(): List<RepositoryItemEntity> {
    return map {
        RepositoryItemEntity(
            name = it.name ?: "",
            avatar = it.owner?.avatarUrl ?: "",
            score = it.score?.takeIf { s -> s.toDoubleOrNull() != null } ?: "0",
            url = it.url ?: "",
            description = it.description ?: "",
            language = it.language ?: ""
        )
    }
}