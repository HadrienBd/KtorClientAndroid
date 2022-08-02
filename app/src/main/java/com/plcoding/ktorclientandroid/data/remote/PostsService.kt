package com.plcoding.ktorclientandroid.data.remote

import com.plcoding.ktorclientandroid.data.remote.dto.PostRequest
import com.plcoding.ktorclientandroid.data.remote.dto.PostResponse
import io.ktor.client.*
import io.ktor.client.engine.android.*
import io.ktor.client.plugins.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.plugins.logging.*
import io.ktor.client.plugins.resources.*
import io.ktor.serialization.kotlinx.json.*
import kotlinx.serialization.json.Json

interface PostsService {

    suspend fun getPosts(): List<PostResponse>

    suspend fun getPostById(id: Long) : PostResponse?

    suspend fun createPost(postRequest: PostRequest): PostResponse?

    companion object {
        fun create(): PostsService {
            return PostsServiceImpl(
                client = HttpClient(Android) {
                    install(Logging) {
                        level = LogLevel.ALL
                    }
                    install(ContentNegotiation) {
                        json(Json {
                            prettyPrint = true
                            ignoreUnknownKeys = true
                            coerceInputValues = true
                        })
                    }
                    install(Resources)
                    defaultRequest {
                        url(HttpRoutes.BASE_URL)
                        /*host = HttpRoutes.HOST
                        url { protocol = URLProtocol.HTTPS }*/
                    }
                }
            )
        }
    }
}

