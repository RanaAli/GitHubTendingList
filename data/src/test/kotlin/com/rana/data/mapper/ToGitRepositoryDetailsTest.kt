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

    @Test
    fun `toRepositoryItemEntity with large list maps all items correctly`() {
        val largeList = (1..1000).map {
            RepositoryItemResponse(
                name = "Repo$it",
                owner = OwnerResponse(avatarUrl = "avatar.url/$it"),
                score = it.toString(),
                url = "http://example.com/$it",
                description = "Description $it",
                language = if (it % 2 == 0) "Kotlin" else "Java"
            )
        }
        val result = largeList.toRepositoryItemEntity()
        assertEquals(1000, result.size)
        assertEquals("Repo1", result[0].name)
        assertEquals("avatar.url/1000", result[999].avatar)
    }

    @Test
    fun `toRepositoryItemEntity with boundary values maps correctly`() {
        val longString = "a".repeat(10000)
        val specialChars = "!@#$%^&*()_+-=[]{}|;':,./<>?`~"
        val responseList = listOf(
            RepositoryItemResponse(
                name = longString,
                owner = OwnerResponse(avatarUrl = specialChars),
                score = "9999999999",
                url = longString,
                description = specialChars,
                language = longString
            )
        )
        val result = responseList.toRepositoryItemEntity()
        assertEquals(longString, result[0].name)
        assertEquals(specialChars, result[0].avatar)
        assertEquals("9999999999", result[0].score)
        assertEquals(longString, result[0].url)
        assertEquals(specialChars, result[0].description)
        assertEquals(longString, result[0].language)
    }

    @Test
    fun `toRepositoryItemEntity with invalid score value maps to default`() {
        val responseList = listOf(
            RepositoryItemResponse(
                name = "RepoInvalidScore",
                owner = OwnerResponse(avatarUrl = "avatar.url/invalid"),
                score = "not_a_number",
                url = "url_invalid",
                description = "desc_invalid",
                language = "Kotlin"
            )
        )
        val result = responseList.toRepositoryItemEntity()
        // If your mapping sets score to "0" for invalid values, check for that
        assertEquals("0", result[0].score)
    }
}