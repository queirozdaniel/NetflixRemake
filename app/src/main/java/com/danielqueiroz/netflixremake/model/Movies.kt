package com.danielqueiroz.netflixremake.model

import com.google.gson.annotations.SerializedName

data class Categories(@SerializedName("category") val categories: List<Category>)

data class Category(
        @SerializedName("title") var name: String = "",
        @SerializedName("movie") var movies: MutableList<Movie> = arrayListOf())

data class Movie(
        var id: Int = 0,
        @SerializedName("cover_url") var coverUrl: String = "",
        var title: String = "",
        var desc: String = "",
        var cast: String = "")

data class MovieDetail(var id: Int = 0,
                       @SerializedName("cover_url") var coverUrl: String = "",
                       var title: String = "",
                       var desc: String = "",
                       var cast: String = "",
                       val movie: Movie,
                       @SerializedName("movie") val moviesSimilar: List<Movie>)