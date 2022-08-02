package com.plcoding.ktorclientandroid.data.remote

import com.plcoding.ktorclientandroid.data.remote.dto.PostRequest
import com.plcoding.ktorclientandroid.data.remote.dto.PostResponse
import com.plcoding.ktorclientandroid.data.remote.dto.Posts
import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.plugins.*
import io.ktor.client.plugins.resources.*
import io.ktor.client.request.*
import io.ktor.http.*

class PostsServiceImpl(
    private val client: HttpClient
) : PostsService {

    override suspend fun getPosts(): List<PostResponse> {
        return try {
            client.get(Posts()).body()
        } catch(e: ClientRequestException) { // 4xx - responses
            println("Error: ${e.response.status.description}")
            emptyList()
        } catch(e: ServerResponseException) { // 5xx - responses
            println("Error: ${e.response.status.description}")
            emptyList()
        } catch(e: Exception) {
            println("Error: ${e.message}")
            emptyList()
        }
    }

    override suspend fun getPostById(id: Long): PostResponse? {
        return try {
            client.get(Posts.Id(id = id)).body()
        } catch(e: Exception) {
            null
        }
    }

    override suspend fun createPost(postRequest: PostRequest): PostResponse? {
        return try {
            client.post(HttpRoutes.POSTS) {
                contentType(ContentType.Application.Json)
                setBody(postRequest)
            }.body()
        } catch(e: Exception) {
            println("Error: ${e.message}")
            null
        }
    }
}


