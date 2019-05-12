package com.pixabayapp

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import com.Util.ApiInterface
import com.example.gconnect.Resource
import com.nhaarman.mockitokotlin2.spy
import com.nhaarman.mockitokotlin2.times
import com.nhaarman.mockitokotlin2.verify
import com.pixabayapp.Model.SearchResult
import com.pixabayapp.Repository.PixabayRepository
import com.pixabayapp.ViewModel.VmImageSearch
import io.reactivex.Maybe
import org.junit.Assert.assertNotNull
import org.junit.Before
import org.junit.ClassRule
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4
import org.mockito.ArgumentMatchers.*
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.Mockito.`when`
import org.mockito.Mockito.mock
import org.mockito.MockitoAnnotations.initMocks
import java.net.SocketException


@RunWith(JUnit4::class)
class VmImageSearchTest {

    companion object {
        @ClassRule
        @JvmField
        val schedulers = RxImmediateSchedulerRule()
    }

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    @Mock
    lateinit var apiInterface: ApiInterface

    @Mock
    lateinit var vmImageSearch: VmImageSearch

    @Mock
    lateinit var pixabayRepository: PixabayRepository


    @Before
    fun setUp() {
//        RxAndroidPlugins.setInitMainThreadSchedulerHandler { scheduler -> Schedulers.trampoline() }
        initMocks(this)
        this.vmImageSearch = VmImageSearch()
    }

    @Test
    fun fetchImageSearchRepositoriesSuccess() {

        `when`(this.pixabayRepository.getImageSearch(anyString(), anyString())).thenAnswer {
            return@thenAnswer Maybe.just(anyList<Resource<SearchResult>>())
        }

        `when`(this.apiInterface.getImageSearch(anyString(), anyString(),anyString(),anyString() )).thenAnswer {
            return@thenAnswer Maybe.just(anyList<Resource<SearchResult>>())
        }

        val observer = mock(Observer::class.java) as Observer<Resource<SearchResult>>
        this.vmImageSearch.searchAPIResult.observeForever(observer)

        this.vmImageSearch.getImageSearchResult(anyString(), anyString())
//        this.vmImageSearch.getImageSearchResult(eq("yellow"), eq("4"))
//        this.vmImageSearch.getImageSearchResult(any(), any())

        assertNotNull(this.vmImageSearch.searchAPIResult.value)
//        assertEquals(ResourceState.SUCCESS, this.vmImageSearch.searchAPIResult.value?.state)
//        assertEquals(LiveDataResult.Status.SUCCESS, this.vmImageSearch.searchAPIResult.value?.status)
    }

    @Test
    fun fetchImageSearchRepositoriesError() {

        `when`(this.pixabayRepository.getImageSearch(anyString(), anyString())).thenAnswer {
            return@thenAnswer Maybe.error<SocketException>(SocketException("No network here"))
        }

        `when`(this.apiInterface.getImageSearch(anyString(), anyString(),anyString(),anyString() )).thenAnswer {
            return@thenAnswer Maybe.error<SocketException>(SocketException("No network here"))
        }

        //test the add functionality
        val observer = Mockito.mock(Observer::class.java) as Observer<Resource<SearchResult>>
        this.vmImageSearch.searchAPIResult.observeForever(observer)

        this.vmImageSearch.getImageSearchResult(anyString(), anyString())

        assertNotNull(this.vmImageSearch.searchAPIResult.value)
    }



    @Test
    fun setLoadingImageSearchVisibilitySuccess() {

        `when`(this.pixabayRepository.getImageSearch(anyString(), anyString())).thenAnswer {
            return@thenAnswer Maybe.just(anyList<Resource<SearchResult>>())
        }

        `when`(this.apiInterface.getImageSearch(anyString(), anyString(),anyString(),anyString() )).thenAnswer {
            return@thenAnswer Maybe.just(anyList<Resource<SearchResult>>())
        }

        val spiedViewModel = spy(this.vmImageSearch)

        spiedViewModel.getImageSearchResult(anyString(), anyString())
        verify(spiedViewModel, times(2)).setLoadingVisibility(anyBoolean())
    }


    @Test
    fun setLoadingImageSearchVisibilityFailure() {

        `when`(this.pixabayRepository.getImageSearch(anyString(), anyString())).thenAnswer {
            return@thenAnswer Maybe.error<SocketException>(SocketException("No network here"))
        }

        `when`(this.apiInterface.getImageSearch(anyString(), anyString(),anyString(),anyString() )).thenAnswer {
            return@thenAnswer Maybe.error<SocketException>(SocketException("No network here"))
        }

        val spiedViewModel = spy(this.vmImageSearch)

        spiedViewModel.getImageSearchResult(anyString(), anyString())
        verify(spiedViewModel, times(2)).setLoadingVisibility(anyBoolean())
    }



    @Test
    fun setLoadingVisibilityNoData() {

        `when`(this.pixabayRepository.getImageSearch(anyString(), anyString())).thenReturn(null)

        `when`(this.apiInterface.getImageSearch(anyString(), anyString(),anyString(),anyString() )).thenReturn(null)

        val spiedViewModel = spy(this.vmImageSearch)

        spiedViewModel.getImageSearchResult(anyString(), anyString())
        verify(spiedViewModel, times(2)).setLoadingVisibility(anyBoolean())
    }

}