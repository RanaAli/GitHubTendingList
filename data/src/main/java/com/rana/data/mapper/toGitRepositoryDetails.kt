package com.rana.data.mapper

import com.rana.data.models.response.RepositoryItemResponse
import com.rana.data.models.entity.RepositoryItemEntity

fun List<RepositoryItemResponse>.toRepositoryEntity(): List<RepositoryItemEntity> {
    return map {
        RepositoryItemEntity(
            name = it.name ?: "",
            avatar = it.owner?.avatarUrl ?: "",
            score = it.score ?: 0.0,
            url = it.url ?: "",
            description = it.description ?: "",
            language = it.language ?: ""
        )
    }
}