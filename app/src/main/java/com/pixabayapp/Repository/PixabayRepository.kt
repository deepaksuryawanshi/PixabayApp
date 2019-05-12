package com.pixabayapp.Repository

import com.Util.ApiClient
import com.pixabayapp.AppController
import com.pixabayapp.Model.SearchResult

class PixabayRepository {

    var image_type: String = "photo"

    val apiClient by lazy {
        ApiClient.create()
    }

    fun getImageSearch(searchString: String, pageNo: String): io.reactivex.Observable<SearchResult>{
        return apiClient.getImageSearch(AppController.KEY, searchString, image_type, pageNo)
    }

}