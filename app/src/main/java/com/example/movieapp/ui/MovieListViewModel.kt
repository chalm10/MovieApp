package com.example.movieapp.ui

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.movieapp.data.Movie
import com.example.movieapp.data.MovieListRepository
import com.example.movieapp.data.network.LoadingStatus
import com.example.movieapp.data.network.OttApi
import com.example.movieapp.data.network.OttApiService
import kotlinx.coroutines.*


class MovieListViewModel(application: Application) : AndroidViewModel(application) {
    private val repo : MovieListRepository = MovieListRepository(application)
    val movies : LiveData<List<Movie>> = repo.getMovies()

    private val _loadingStatus = MutableLiveData<LoadingStatus>()
    val loadingStatus : LiveData<LoadingStatus>
        get() = _loadingStatus



    fun fetchFromNetwork(){
        _loadingStatus.value = LoadingStatus.loading()
        viewModelScope.launch {
            _loadingStatus.value = withContext(Dispatchers.IO){
                repo.fetchFromNetwork()
            }
        }
    }

    fun refreshData(){
        viewModelScope.launch(Dispatchers.IO) {
            repo.deleteAllData()
        }
        fetchFromNetwork()
    }



    private val _response = MutableLiveData<String>()
    val response : LiveData<String>
        get() = _response

    private var  viewModelJob = Job()
    private val coroutinesScope = CoroutineScope(viewModelJob + Dispatchers.Main)
    init {
        getOttDetails()
    }

    private fun getOttDetails() {
        coroutinesScope.launch {
            var getOttDetailsDeferred = OttApi.retrofitService.getMovieOttDetails("tt1187043")
            try {
                var result = getOttDetailsDeferred.await().streamingAvailability.country.IN
                _response.value = "Success : $result"
            }catch (t : Throwable){
                _response.value = "Failure: " + t.message
            }
        }
    }

    override fun onCleared() {
        super.onCleared()
        viewModelJob.cancel()
    }
}