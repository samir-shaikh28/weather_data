package com.example.weather.ui.viewmodels

import android.util.Log
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.weather.data.datastore.WeatherDataStore
import com.example.weather.data.db.WeatherDao
import com.example.weather.data.network.Error
import com.example.weather.data.network.Failure
import com.example.weather.data.network.Result
import com.example.weather.data.network.Success
import com.example.weather.data.responses.CurrentWeather
import com.example.weather.repository.WeatherRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.scopes.ViewModelScoped
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.take
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeFragmentViewModel @Inject constructor(
    private val repository: WeatherRepository,
    private val dataStore: DataStore<Preferences>
) :
    ViewModel() {

    private var _currentWeather = SingleLiveEvent<Result<CurrentWeather>>()
    val currentWeather: LiveData<Result<CurrentWeather>>
        get() = _currentWeather




    fun fetchWeatherData() {
        val exceptionHandler = CoroutineExceptionHandler { _, throwable ->
            _currentWeather.postValue(Failure(throwable))
        }

        viewModelScope.launch(exceptionHandler + Dispatchers.IO) {
            val latLon = async { WeatherDataStore.getLatitudeLongitude(dataStore).take(1) }
            latLon.await().collect {
                val response = repository.fetchWeatherData(it)
                if (response.current != null) {
                    _currentWeather.postValue(Success(response))
                } else {
                    _currentWeather.postValue(Error(response))
                }
            }
        }
    }
}