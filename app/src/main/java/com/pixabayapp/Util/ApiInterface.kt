package com.Util

import com.pixabayapp.Model.SearchResult
import retrofit2.http.GET
import retrofit2.http.Query

interface ApiInterface {

     // https://pixabay.com/api/?key=8043492-ac2d35c3a6sdf5433afad06a1e5&q=dogs&image_type=photo
    @GET("api/?")
    fun getImageSearch(@Query("key") key: String, @Query("q") q: String, @Query("image_type") image_type: String, @Query("page") page: String): io.reactivex.Observable<SearchResult>
}