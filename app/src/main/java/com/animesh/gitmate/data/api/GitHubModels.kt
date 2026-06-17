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

data class GitHubContentItem(
    val name: String,
    val path: String,
    val type: String, // "file" or "dir"
    val size: Int,
    val download_url: String?
)

data class GitHubFileContent(
    val name: String,
    val path: String,
    val content: String, // Base64 encoded
    val encoding: String
)