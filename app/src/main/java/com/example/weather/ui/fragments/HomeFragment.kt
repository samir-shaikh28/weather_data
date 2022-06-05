package com.example.weather.ui.fragments

import android.location.Location
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.ActionBar
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.bumptech.glide.RequestManager
import com.example.weather.R
import com.example.weather.data.network.Error
import com.example.weather.data.network.Failure
import com.example.weather.data.network.Success
import com.example.weather.data.responses.CurrentResponse
import com.example.weather.data.responses.CurrentWeather
import com.example.weather.data.responses.LocationResponse
import com.example.weather.databinding.FragmentHomeBinding
import com.example.weather.listener.OnBookMarkClickListener
import com.example.weather.repository.WeatherRepository
import com.example.weather.ui.adapters.GenericRecyclerAdapter
import com.example.weather.ui.viewholders.AbstractViewHolder
import com.example.weather.ui.viewholders.CurrentWeatherVH
import com.example.weather.ui.viewholders.DayViewHolder
import com.example.weather.ui.viewholders.HoursViewHolder
import com.example.weather.ui.viewmodels.FavouriteViewModel
import com.example.weather.ui.viewmodels.HomeFragmentViewModel
import com.example.weather.utils.VerticalSpaceItemDecoration
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class HomeFragment : Fragment(), OnBookMarkClickListener {

    private lateinit var mBinding: FragmentHomeBinding

    private lateinit var navController: NavController

    private val actionBar: ActionBar?
        get() = appCompatActivity?.supportActionBar

    private val appCompatActivity: AppCompatActivity?
        get() = activity as? AppCompatActivity?

    private var isCurrentCityFav = false

    private val viewModel: HomeFragmentViewModel by viewModels()
    private val favViewModel: FavouriteViewModel by viewModels()
    private lateinit var requestManager: RequestManager

    private val adapter by lazy { GenericRecyclerAdapter() }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        mBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_home, container, false)
        requestManager = Glide.with(this@HomeFragment)
        navController = Navigation.findNavController(requireActivity(), R.id.nav_host_fragment)
        setHasOptionsMenu(true)
        setUpRecyclerView()
        initToolBar()
        return mBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        favViewModel.isCurrentCityIsFavourite()
        updateIfCurrentCityFav()
        fetchWeatherData()
        observeWeatherData()
    }

    private fun updateIfCurrentCityFav() {
        favViewModel.isCurrentCityFav.observe(viewLifecycleOwner, Observer {
            isCurrentCityFav = it
        })
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.home_menu, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == R.id.fav) {
            navController.navigate(R.id.action_homeFragment_to_favouriteCityFragment)
        }
        return super.onOptionsItemSelected(item)
    }

    private fun initToolBar() {
        setSupportActionBar(mBinding.toolbar)
        actionBar?.run {
            setDisplayHomeAsUpEnabled(false)
            setDisplayShowHomeEnabled(false)
            title = "Weather App"
        }
    }

    private fun setSupportActionBar(toolbar: Toolbar) {
        appCompatActivity?.setSupportActionBar(toolbar)
    }

    private fun fetchWeatherData() {
        showProgress()
        viewModel.fetchWeatherData()
    }

    private fun setUpRecyclerView() {
        mBinding.recycler.layoutManager = LinearLayoutManager(requireContext())
        mBinding.recycler.addItemDecoration(
            VerticalSpaceItemDecoration(
                resources.getDimension(R.dimen.vertical_margin_half).toInt()
            )
        )
        mBinding.recycler.adapter = adapter
    }

    private fun observeWeatherData() {
        viewModel.currentWeather.observe(viewLifecycleOwner) { outcome ->
            when (outcome) {
                is Success -> {
                    handleSuccessResponse(outcome.data)
                }
                is Error -> {
                    handleError("something went wrong")
                }
                is Failure -> {
                    handleError(outcome.throwable.localizedMessage)
                }
            }
        }
    }

    private fun handleError(error: String?) {
        hideProgress()
        // Handle Error Here
    }

    private fun hideProgress() {
        mBinding.progress.visibility = View.GONE
    }

    private fun showProgress() {
        mBinding.progress.visibility = View.VISIBLE
    }

    private fun handleSuccessResponse(data: CurrentWeather) {
        hideProgress()
        adapter.listCustomModel = getListOfAbstractVH(data)
        adapter.notifyDataSetChanged()
    }

    private fun getListOfAbstractVH(data: CurrentWeather): List<AbstractViewHolder> {
        val list = mutableListOf<AbstractViewHolder>()
        list.add(CurrentWeatherVH(data.current, data.location, isCurrentCityFav, requestManager, this@HomeFragment))
        list.add(HoursViewHolder(data.forecastData?.forecastday?.get(0)?.hour, requestManager))
        list.add(DayViewHolder(data.forecastData?.forecastday, requestManager))
        return list
    }

    override fun addToFavourites(location: LocationResponse?) {
        location?.apply {
            favViewModel.addToFavourite("${lat},${lon}", name, region)
        }
    }

    override fun removeFromFavourite(latLon: String) {
        favViewModel.removeFromFavourite(latLon)
    }


}