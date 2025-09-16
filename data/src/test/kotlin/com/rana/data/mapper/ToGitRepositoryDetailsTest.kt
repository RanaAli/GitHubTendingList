package com.rana.data.mapper

import com.rana.data.models.response.OwnerResponse
import com.rana.data.models.response.RepositoryItemResponse
import com.rana.domain.entity.RepositoryItemEntity
import org.junit.Assert.assertEquals
import org.junit.Test

class ToGitRepositoryDetailsTest {

    @Test
    fun `toRepositoryItemEntity with empty list returns empty list`() {
        val emptyResponseList = emptyList<RepositoryItemResponse>()
        val result = emptyResponseList.toRepositoryItemEntity()
        assertEquals(emptyList<RepositoryItemEntity>(), result)
    }

    @Test
    fun `toRepositoryItemEntity with full data maps correctly`() {
        val responseList = listOf(
            RepositoryItemResponse(
                name = "Repo1",
                owner = OwnerResponse(avatarUrl = "avatar.url/repo1"),
                score = "123",
                url = "http://example.com/repo1",
                description = "Description for Repo1",
                language = "Kotlin"
            ),
            RepositoryItemResponse(
                name = "Repo2",
                owner = OwnerResponse(avatarUrl = "avatar.url/repo2"),
                score = "456",
                url = "http://example.com/repo2",
                description = "Description for Repo2",
                language = "Java"
            )
        )

        val expectedEntities = listOf(
            RepositoryItemEntity(
                name = "Repo1",
                avatar = "avatar.url/repo1",
                score = "123",
                url = "http://example.com/repo1",
                description = "Description for Repo1",
                language = "Kotlin"
            ),
            RepositoryItemEntity(
                name = "Repo2",
                avatar = "avatar.url/repo2",
                score = "456",
                url = "http://example.com/repo2",
                description = "Description for Repo2",
                language = "Java"
            )
        )

        val result = responseList.toRepositoryItemEntity()
        assertEquals(expectedEntities, result)
    }

    @Test
    fun `toRepositoryItemEntity with partial null data maps to defaults`() {
        val responseList = listOf(
            RepositoryItemResponse( // All nulls
                name = null,
                owner = null,
                score = null,
                url = null,
                description = null,
                language = null
            ),
            RepositoryItemResponse( // Owner is not null, but avatarUrl is null
                name = "RepoWithNullAvatar",
                owner = OwnerResponse(avatarUrl = null),
                score = "10",
                url = "url2",
                description = "Desc2",
                language = "Lang2"
            ),
            RepositoryItemResponse( // Only some fields null
                name = "RepoPartial",
                owner = OwnerResponse(avatarUrl = "avatar.url/partial"),
                score = null, // Score is null
                url = "http://example.com/partial",
                description = null, // Description is null
                language = "Go"
            )
        )

        val expectedEntities = listOf(
            RepositoryItemEntity( // Defaults for all nulls
                name = "",
                avatar = "",
                score = "0",
                url = "",
                description = "",
                language = ""
            ),
            RepositoryItemEntity( // Default for null avatarUrl
                name = "RepoWithNullAvatar",
                avatar = "",
                score = "10",
                url = "url2",
                description = "Desc2",
                language = "Lang2"
            ),
            RepositoryItemEntity( // Defaults for score and description
                name = "RepoPartial",
                avatar = "avatar.url/partial",
                score = "0",
                url = "http://example.com/partial",
                description = "",
                language = "Go"
            )
        )

        val result = responseList.toRepositoryItemEntity()
        assertEquals(expectedEntities, result)
    }
}