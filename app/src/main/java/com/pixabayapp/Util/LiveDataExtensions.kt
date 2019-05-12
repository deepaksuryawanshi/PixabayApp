package com.sanogueralorenzo.presentation

import androidx.lifecycle.MutableLiveData
import com.example.gconnect.Resource
import com.example.gconnect.ResourceState

fun <T> MutableLiveData<Resource<T>>.setSuccess(data: T) =
    postValue(Resource(ResourceState.SUCCESS, data))

fun <T> MutableLiveData<Resource<T>>.setLoading() =
//    postValue(Resource(ResourceState.LOADING, value?.data))
    postValue(Resource(ResourceState.LOADING))

fun <T> MutableLiveData<Resource<T>>.setError(message: String? = null) =
//    postValue(Resource(ResourceState.ERROR, value?.data, message))
    postValue(Resource(ResourceState.ERROR, null, message))

//fun <T> succes(data: T?) = LiveDataResult(Status.SUCCESS, data)
//fun <T> loading() = LiveDataResult<T>(Status.LOADING)
//fun <T> error(err: Throwable?) = LiveDataResult<T>(Status.ERROR, null, err)
