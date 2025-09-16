package com.rana.data.models.response

import com.google.gson.Gson
import org.junit.Assert.*
import org.junit.Test

class RepositoryResponseTest {
    private val gson = Gson()

    @Test
    fun `deserialize typical JSON response`() {
        val json = """
            {
              "items": [
                {
                  "name": "Repo1",
                  "owner": { "avatar_url": "avatar.url/1" },
                  "stargazers_count": "123",
                  "url": "http://example.com/1",
                  "description": "Desc1",
                  "language": "Kotlin"
                },
                {
                  "name": "Repo2",
                  "owner": { "avatar_url": "avatar.url/2" },
                  "stargazers_count": "456",
                  "url": "http://example.com/2",
                  "description": "Desc2",
                  "language": "Java"
                }
              ]
            }
        """
        val response = gson.fromJson(json, RepositoryResponse::class.java)
        assertNotNull(response.items)
        response.items?.let { items ->
            assertEquals(2, items.size)
            val firstItem = items[0]
            assertEquals("Repo1", firstItem.name)
            assertEquals("avatar.url/1", firstItem.owner?.avatarUrl)
            assertEquals("123", firstItem.score)
            assertEquals("Kotlin", firstItem.language)
        }
    }

    @Test
    fun `deserialize JSON with missing fields uses defaults`() {
        val json = """
            {
              "items": [
                {
                  "name": "RepoMissingFields"
                }
              ]
            }
        """
        val response = gson.fromJson(json, RepositoryResponse::class.java)
        response.items?.firstOrNull()?.let { item ->
            assertEquals("RepoMissingFields", item.name)
            assertNull(item.owner)
            assertNull(item.score)
            assertNull(item.url)
            assertNull(item.description)
            assertNull(item.language)
        }
    }

    @Test
    fun `deserialize JSON with empty items list`() {
        val json = """
            {"items": []}
        """
        val response = gson.fromJson(json, RepositoryResponse::class.java)
        assertNotNull(response.items)
        assertTrue(response.items?.isEmpty() ?: false)
    }

    @Test
    fun `deserialize JSON with null owner and null avatarUrl`() {
        val json = """
            {
              "items": [
                {
                  "name": "RepoNullOwner",
                  "owner": null,
                  "stargazers_count": "10"
                },
                {
                  "name": "RepoNullAvatar",
                  "owner": { "avatar_url": null },
                  "stargazers_count": "20"
                }
              ]
            }
        """
        val response = gson.fromJson(json, RepositoryResponse::class.java)
        response.items?.let { items ->
            val item1 = items[0]
            assertEquals("RepoNullOwner", item1.name)
            assertNull(item1.owner)
            assertEquals("10", item1.score)

            val item2 = items[1]
            assertEquals("RepoNullAvatar", item2.name)
            assertNotNull(item2.owner)
            assertNull(item2.owner?.avatarUrl)
            assertEquals("20", item2.score)
        }
    }

    @Test
    fun `serialize RepositoryItemResponse to JSON uses correct field names`() {
        val item = RepositoryItemResponse(
            name = "Repo",
            owner = OwnerResponse(avatarUrl = "avatar.url"),
            score = "42",
            url = "url",
            description = "desc",
            language = "Kotlin"
        )
        val json = gson.toJson(item)
        assertTrue(json.contains("stargazers_count"))
        assertTrue(json.contains("avatar_url"))
        assertTrue(json.contains("name"))
        assertTrue(json.contains("description"))
        assertTrue(json.contains("language"))
    }

    @Test(expected = Exception::class)
    fun `deserialize invalid JSON throws exception`() {
        val json = """
            {"items": ["not_an_object"]}
        """
        gson.fromJson(json, RepositoryResponse::class.java)
    }
}
