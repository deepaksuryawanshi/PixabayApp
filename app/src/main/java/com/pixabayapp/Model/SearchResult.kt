package com.pixabayapp.Model

data class SearchResult(
    val hits: ArrayList<Hit> = ArrayList<Hit>(),
    val total: Int? = 0, // 18796
    val totalHits: Int? = 0 // 500
) {
    data class Hit(
        val comments: Int? = 0, // 36
        val downloads: Int? = 0, // 31648
        val favorites: Int? = 0, // 282
        val id: Int? = 0, // 887443
        val imageHeight: Int? = 0, // 3005
        val imageSize: Int? = 0, // 2611531
        val imageWidth: Int? = 0, // 3867
        val largeImageURL: String? = "", // https://pixabay.com/get/e13db60b2cf71c22d2524518b74d4492e174e5d31cac104490f7c370a4eab2b0_1280.jpg
        val likes: Int? = 0, // 353
        val pageURL: String? = "", // https://pixabay.com/photos/flower-life-crack-desert-drought-887443/
        val previewHeight: Int? = 0, // 116
        val previewURL: String? = "", // https://cdn.pixabay.com/photo/2015/08/13/20/06/flower-887443_150.jpg
        val previewWidth: Int? = 0, // 150
        val tags: String? = "", // flower, life, crack
        val type: String? = "", // photo
        val user: String? = "", // klimkin
        val userImageURL: String? = "", // https://cdn.pixabay.com/user/2017/07/18/13-46-18-393_250x250.jpg
        val user_id: Int? = 0, // 1298145
        val views: Int? = 0, // 64410
        val webformatHeight: Int? = 0, // 497
        val webformatURL: String? = "", // https://pixabay.com/get/e13db60b2cf71c22d2524518b74d4492e174e5d31cac104490f7c370a4eab2b0_640.jpg
        val webformatWidth: Int? = 0 // 640
    )
}