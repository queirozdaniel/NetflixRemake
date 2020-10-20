package com.danielqueiroz.netflixremake.model

data class Category(
        var name: String = "",
        var movies: MutableList<Movie> = arrayListOf())

data class Movie(
        var id: Int = 0,
        var coverUrl: String = "",
        var title: String = "",
        var desc: String = "",
        var cast: String = "")

data class MovieDetail(val movie: Movie, val moviesSimilar: List<Movie>)