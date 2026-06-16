package com.animesh.gitmate.data.api

import retrofit2.http.GET
import retrofit2.http.Path

interface GitHubApi {
    @GET("users/{username}")
    suspend fun getUser(@Path("username") username: String): GitHubUser

    @GET("users/{username}/repos")
    suspend fun getUserRepos(@Path("username") username: String): List<GitHubRepo>
}