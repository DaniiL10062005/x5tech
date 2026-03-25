package com.example.x5tech.model.repository

import com.example.x5tech.model.domain.BankCard
import io.ktor.client.call.body
import io.ktor.client.HttpClient
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.request.get
import io.ktor.client.request.header
import io.ktor.serialization.kotlinx.json.json
import io.ktor.http.isSuccess
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json

class BinlistCardRepository(
    private val json: Json = Json {
        ignoreUnknownKeys = true
    },
    private val httpClient: HttpClient = createHttpClient(json),
) : CardRepository {

    private val savedCards = mutableListOf<BankCard>()

    override suspend fun getBankByBin(bin: String): String? {
        if (bin.length !in BIN_LENGTH_RANGE || !bin.all(Char::isDigit)) {
            return null
        }

        LocalBankByBinResolver.resolve(bin)?.let { localBankName ->
            return localBankName
        }

        return runCatching {
            val response = httpClient.get("$BASE_URL/$bin") {
                header(ACCEPT_VERSION_HEADER, ACCEPT_VERSION)
            }
            if (!response.status.isSuccess()) {
                return@runCatching null
            }

            response.body<BinlistResponse>().bank?.name?.takeIf(String::isNotBlank)
        }.getOrNull() ?: LocalBankByBinResolver.resolve(bin)
    }

    override suspend fun saveCard(card: BankCard) {
        savedCards += card
    }

    @Serializable
    private data class BinlistResponse(
        val bank: BankInfo? = null,
    )

    @Serializable
    private data class BankInfo(
        @SerialName("name")
        val name: String? = null,
    )

    private companion object {
        const val BASE_URL = "https://lookup.binlist.net"
        const val ACCEPT_VERSION_HEADER = "Accept-Version"
        const val ACCEPT_VERSION = "3"

        val BIN_LENGTH_RANGE = 6..8

        fun createHttpClient(json: Json): HttpClient {
            return HttpClient {
                expectSuccess = false

                install(ContentNegotiation) {
                    json(json)
                }
            }
        }
    }
}
