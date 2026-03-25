package com.example.x5tech.model.repository

import io.ktor.client.HttpClient
import io.ktor.client.engine.mock.MockEngine
import io.ktor.client.engine.mock.respond
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.http.ContentType
import io.ktor.http.HttpHeaders
import io.ktor.http.HttpStatusCode
import io.ktor.http.headersOf
import io.ktor.serialization.kotlinx.json.json
import kotlinx.coroutines.test.runTest
import kotlinx.serialization.json.Json
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNull

internal class BinlistCardRepositoryTest {

    private val json = Json {
        ignoreUnknownKeys = true
    }

    @Test
    fun `should resolve bank name from binlist response`() = runTest {
        val repository = createRepository(
            responseBody = """
                {
                  "bank": {
                    "name": "Test Bank"
                  }
                }
            """.trimIndent(),
            statusCode = HttpStatusCode.OK,
        )

        assertEquals("Test Bank", repository.getBankByBin("411111"))
    }

    @Test
    fun `should fallback to local bank resolver when binlist bank is empty`() = runTest {
        val repository = createRepository(
            responseBody = """
                {
                  "scheme": "mastercard",
                  "bank": {}
                }
            """.trimIndent(),
            statusCode = HttpStatusCode.OK,
        )

        assertEquals("SberBank", repository.getBankByBin("54696700"))
    }

    @Test
    fun `should fallback to local bank resolver when request fails`() = runTest {
        val repository = createRepository(
            responseBody = """{"error":"not found"}""",
            statusCode = HttpStatusCode.NotFound,
        )

        assertEquals("SberBank", repository.getBankByBin("54696700"))
    }

    @Test
    fun `should return null for invalid bin`() = runTest {
        val repository = createRepository(
            responseBody = """{}""",
            statusCode = HttpStatusCode.OK,
        )

        assertNull(repository.getBankByBin("12ab"))
    }

    private fun createRepository(
        responseBody: String,
        statusCode: HttpStatusCode,
    ): BinlistCardRepository {
        val httpClient = HttpClient(
            engine = MockEngine { request ->
                respond(
                    content = responseBody,
                    status = statusCode,
                    headers = headersOf(
                        HttpHeaders.ContentType,
                        ContentType.Application.Json.toString(),
                    ),
                )
            },
        ) {
            install(ContentNegotiation) {
                json(json)
            }
            expectSuccess = false
        }

        return BinlistCardRepository(
            json = json,
            httpClient = httpClient,
        )
    }
}
