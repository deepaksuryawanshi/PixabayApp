package com.pixabayapp.ViewModel

import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.gconnect.Resource
import com.pixabayapp.Model.SearchResult
import com.pixabayapp.Repository.PixabayRepository
import com.sanogueralorenzo.presentation.setError
import com.sanogueralorenzo.presentation.setLoading
import com.sanogueralorenzo.presentation.setSuccess
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers

/**
 *  View Model class to executes api for movie search screen.
 */
class VmImageSearch : ViewModel() {

    // Holds reference of the search image live data result.
    val searchAPIResult = MediatorLiveData<Resource<SearchResult>>()

    // Holds reference of the composite disposable object.
    var mCompositeDisposable: CompositeDisposable? = CompositeDisposable()

    // Holds reference of the repository class object.
    val pixabayRepository: PixabayRepository = PixabayRepository()

    // Holds reference of the image result check
    val loadingLiveDataImageResult = MutableLiveData<Boolean>()

    /**
     * Get search API result from server.
     */
    fun getImageSearchResult(searchString: String, pageNo: String) {

        searchAPIResult.setLoading()

        this.setLoadingVisibility(true)
        mCompositeDisposable?.add(
            pixabayRepository.getImageSearch(searchString, pageNo)
                .observeOn(AndroidSchedulers.mainThread()) // specifies the scheduler on which an observer will observe this Observable
                .subscribeOn(Schedulers.io()) // tells the source Observable which thread to use for emitting items to the Observer
                .subscribe(this::imageSearchResult, this::handleError)
        )
    }

    /**
     * Handle API success message.
     */
    private fun imageSearchResult(searchResult: SearchResult) {
        this.setLoadingVisibility(true)
        searchAPIResult.setSuccess(searchResult)
    }

    /**
     * Handle API error message.
     */
    private fun handleError(throwable: Throwable) {
        this.setLoadingVisibility(false)
        searchAPIResult.setError(throwable.message)
    }

    /**
     * Set a progress dialog visible on the View
     * @param visible visible or not visible
     */
    fun setLoadingVisibility(visible: Boolean) {
        loadingLiveDataImageResult.postValue(visible)
    }

}