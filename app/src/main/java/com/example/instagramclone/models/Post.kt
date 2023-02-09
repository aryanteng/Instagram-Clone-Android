package com.example.instagramclone.models

data class Post (
    var description: String = "",
    var imgSrc: String = "",
    var createdAt: Long = 0,
    var user: User? = null
    )