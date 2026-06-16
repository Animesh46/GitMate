package com.animesh.gitmate.data.api

import com.google.gson.annotations.SerializedName

data class GitHubUser(
    val login: String,
    val name: String?,
    val bio: String?,
    @SerializedName("avatar_url") val avatarUrl: String,
    val followers: Int,
    val following: Int
)

data class GitHubRepo(
    val name: String,
    val description: String?,
    val language: String?,
    @SerializedName("stargazers_count") val stars: Int
)