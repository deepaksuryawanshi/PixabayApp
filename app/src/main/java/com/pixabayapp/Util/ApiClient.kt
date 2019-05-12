package com.Util

import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

class ApiClient {

    companion object {

        // https://pixabay.com/api/?key=8043492-ac2d35c3a6sdf5433afad06a1e5&q=dogs&image_type=photo
        private const val baseUrl: String = "https://pixabay.com/"

        fun create(): ApiInterface {

            val interceptor1 = HttpLoggingInterceptor()
            interceptor1.level = HttpLoggingInterceptor.Level.BODY

            val okHttpClient = OkHttpClient.Builder()
                    .addInterceptor(headersInterceptor())
                    .connectTimeout(30, TimeUnit.SECONDS) // For testing purposes
                    .readTimeout(30, TimeUnit.SECONDS) // For testing purposes
                    .writeTimeout(30, TimeUnit.SECONDS)
                    .addInterceptor(interceptor1)
//                    .addInterceptor(timeoutInterceptor())
//                    .addInterceptor(connectionTimeOutInterceptor)
                    .build()

            val retrofit = Retrofit.Builder()
                    .baseUrl(baseUrl)
                    .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                    .addConverterFactory(GsonConverterFactory.create())
                    .client(okHttpClient)
                    .build()

            return retrofit.create(ApiInterface::class.java)
        }


        fun headersInterceptor() = Interceptor { chain ->
            chain.proceed(chain.request().newBuilder()
                    .addHeader("Accept", "application/json")
                    .addHeader("Accept-Language", "en")
                    .addHeader("Content-Type", "application/json")
                    .build())
        }

        fun timeoutInterceptor() = Interceptor { chain ->
//            val request = chain.request()
//            chain.withConnectTimeout(60, TimeUnit.MILLISECONDS)
//                    .withReadTimeout(60, TimeUnit.MILLISECONDS)
//                    .withWriteTimeout(60, TimeUnit.MILLISECONDS)
//                    .proceed(request)

            chain.withConnectTimeout(60, TimeUnit.MILLISECONDS)
                    .withReadTimeout(60, TimeUnit.MILLISECONDS)
                    .withWriteTimeout(60, TimeUnit.MILLISECONDS).proceed(chain.request())
        }

    }

}