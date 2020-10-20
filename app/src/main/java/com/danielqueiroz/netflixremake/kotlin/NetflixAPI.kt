package com.danielqueiroz.netflixremake.kotlin

import com.danielqueiroz.netflixremake.model.Category
import retrofit2.Call
import retrofit2.http.GET

interface NetflixAPI {

    @GET("home")
    fun listCategories(): Call<Category>
}