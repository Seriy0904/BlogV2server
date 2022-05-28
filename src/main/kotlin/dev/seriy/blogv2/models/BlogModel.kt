package dev.seriy.blogv2.models

import kotlinx.serialization.Serializable

@Serializable
data class BlogPost(val id:String, val text:String,val userName:String)
val BlogPostList = mutableListOf<BlogPost>()