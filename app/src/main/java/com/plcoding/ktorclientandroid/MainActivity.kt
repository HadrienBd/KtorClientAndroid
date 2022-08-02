package com.plcoding.ktorclientandroid

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Button
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.plcoding.ktorclientandroid.data.remote.PostsService
import com.plcoding.ktorclientandroid.data.remote.dto.PostRequest
import com.plcoding.ktorclientandroid.data.remote.dto.PostResponse
import com.plcoding.ktorclientandroid.ui.theme.KtorClientAndroidTheme
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlin.random.Random

class MainActivity : ComponentActivity() {

    private val service = PostsService.create()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val coroutineScope = rememberCoroutineScope()
            val posts = mutableStateOf<List<PostResponse>>(emptyList())
            val postRequestStatus = mutableStateOf("")
            KtorClientAndroidTheme {
                Surface(color = MaterialTheme.colors.background) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Spacer(modifier = Modifier.height(20.dp))
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceAround
                        ) {
                            Button(onClick = {
                                coroutineScope.launch {
                                    posts.value = service.getPosts()
                                }
                            }) {
                                Text(text = "GET ALL")
                            }
                            Button(onClick = {
                                coroutineScope.launch {
                                    val post = service.getPostById(Random.nextLong(100))
                                    post?.let {
                                        posts.value = listOf(post)
                                    }
                                }
                            }) {
                                Text(text = "GET RANDOM")
                            }
                            Button(onClick = {
                                coroutineScope.launch {
                                    val createdPost =
                                        service.createPost(PostRequest("Body", "Title", 1))
                                    postRequestStatus.value = if (createdPost == null) {
                                        "POST Request : Error"
                                    } else {
                                        "POST Request : Success"
                                    }
                                    posts.value = emptyList()
                                    delay(3000L)
                                    postRequestStatus.value = ""
                                }
                            }) {
                                Text(text = "POST")
                            }
                        }
                        Spacer(modifier = Modifier.height(20.dp))
                        if (posts.value.isEmpty()) {
                            if (postRequestStatus.value.isNotEmpty()) {
                                Text(postRequestStatus.value)
                            } else {
                                Text(text = "No data")
                            }
                        } else {
                            LazyColumn {
                                items(posts.value) {
                                    Column(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .padding(16.dp)
                                    ) {
                                        Text(text = it.title, fontSize = 20.sp)
                                        Spacer(modifier = Modifier.height(4.dp))
                                        Text(text = it.body, fontSize = 14.sp)
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}