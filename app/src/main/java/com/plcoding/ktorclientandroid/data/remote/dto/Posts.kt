package com.plcoding.ktorclientandroid.data.remote.dto

import io.ktor.resources.*
import kotlinx.serialization.Serializable

@Serializable
@Resource("/posts")
class Posts {

    @Serializable
    @Resource("{id}")
    class Id(val parent: Posts = Posts(), val id: Long)
}