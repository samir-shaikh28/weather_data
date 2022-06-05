package com.example.weather.ui.viewmodels

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.weather.data.datastore.WeatherDataStore
import com.example.weather.data.db.entity.FavouriteEntity
import com.example.weather.data.network.Error
import com.example.weather.data.network.Failure
import com.example.weather.data.network.Result
import com.example.weather.data.network.Success
import com.example.weather.data.responses.CurrentWeather
import com.example.weather.repository.WeatherRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.take
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class FavouriteViewModel @Inject constructor(
    private val repository: WeatherRepository,
    private val dataStore: DataStore<Preferences>
) : ViewModel() {

    private var _favList = MutableLiveData<Result<List<FavouriteEntity>>>()
    val favList: LiveData<Result<List<FavouriteEntity>>>
        get() = _favList

    private var _isCurrentCityFav = MutableLiveData<Boolean>()
    val isCurrentCityFav: LiveData<Boolean>
        get() = _isCurrentCityFav


    fun addToFavourite(latLon: String, name: String?, region: String?) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.bookMarkLocation(latLon, name, region)
        }
    }

    fun removeFromFavourite(latLon: String) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.removeFromFavourite(latLon)
        }
    }

    fun isCurrentCityIsFavourite() {
        viewModelScope.launch(Dispatchers.IO) {
            val latLon = async { WeatherDataStore.getLatitudeLongitude(dataStore).take(1) }
            latLon.await().collect {
                _isCurrentCityFav.postValue(repository.isRowIsExist(it))
            }

        }
    }

    fun fetchFavList() {
        val exceptionHandler = CoroutineExceptionHandler { _, throwable ->
            _favList.postValue(Failure(throwable))
        }

        viewModelScope.launch(Dispatchers.IO + exceptionHandler) {
            val response = repository.fetchFavCityList()
            if (response.isNotEmpty()) {
                _favList.postValue(Success(response))
            } else {
                _favList.postValue((Error(response)))
            }
        }
    }
}