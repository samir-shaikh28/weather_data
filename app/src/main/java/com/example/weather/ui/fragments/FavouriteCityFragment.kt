package com.example.weather.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.app.ActionBar
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.weather.R
import com.example.weather.data.db.entity.FavouriteEntity
import com.example.weather.data.network.Failure
import com.example.weather.data.network.Success
import com.example.weather.data.responses.LocationResponse
import com.example.weather.databinding.FragmentFavouritesBinding
import com.example.weather.listener.OnBookMarkClickListener
import com.example.weather.ui.adapters.GenericRecyclerAdapter
import com.example.weather.ui.viewholders.AbstractViewHolder
import com.example.weather.ui.viewholders.FavouriteItemVH
import com.example.weather.ui.viewmodels.FavouriteViewModel
import com.example.weather.utils.VerticalSpaceItemDecoration
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class FavouriteCityFragment : Fragment(), OnBookMarkClickListener {

    private lateinit var mBinding: FragmentFavouritesBinding

    private val actionBar: ActionBar?
        get() = appCompatActivity?.supportActionBar

    private val appCompatActivity: AppCompatActivity?
        get() = activity as? AppCompatActivity?

    private val favViewModel: FavouriteViewModel by viewModels()

    private val adapter by lazy { GenericRecyclerAdapter() }


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        mBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_favourites, container, false)
        setHasOptionsMenu(true)
        setUpRecyclerView()
        initToolBar()
        handleBackPress()
        return mBinding.root
    }

    private fun handleBackPress() {
//        val callback: OnBackPressedCallback = object : OnBackPressedCallback(true /* enabled by default */) {
//            override fun handleOnBackPressed() {
//                // Handle the back button event
//            }
//        }
//        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner, callback)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        fetchFavList()
        observeFavCityList()
    }

    private fun observeFavCityList() {
        favViewModel.favList.observe(viewLifecycleOwner, Observer { outcome ->
            when (outcome) {
                is Success -> {

                    handleSuccessResponse(outcome.data)
                }
                is Error -> {
                    handleError("No Favourite here")
                }
                is Failure -> {
                    handleError(outcome.throwable.localizedMessage)
                }
            }
        })
    }


    private fun handleSuccessResponse(data: List<FavouriteEntity>) {
        hideProgress()
        adapter.listCustomModel = getListOfAbstractVH(data)
        adapter.notifyDataSetChanged()
    }

    private fun handleError(error: String?) {
        hideProgress()
        Toast.makeText(requireContext(), error, Toast.LENGTH_SHORT).show()
        // Handle Error Here
    }

    private fun fetchFavList() {
        showProgress()
        favViewModel.fetchFavList()
    }

    private fun getListOfAbstractVH(data: List<FavouriteEntity>?): List<AbstractViewHolder> {
        val list = mutableListOf<AbstractViewHolder>()
        data?.forEach {
            list.add(FavouriteItemVH(it, this@FavouriteCityFragment))
        }
        return list
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home) {
           // findNavController().popBackStack()
        }
        return super.onOptionsItemSelected(item)
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

    private fun initToolBar() {
        setSupportActionBar(mBinding.toolbar)
        actionBar?.run {
            setDisplayHomeAsUpEnabled(true)
            setDisplayShowHomeEnabled(true)
            title = "Favourite"
        }
    }

    private fun hideProgress() {
        mBinding.progress.visibility = View.GONE
    }

    private fun showProgress() {
        mBinding.progress.visibility = View.VISIBLE
    }


    private fun setSupportActionBar(toolbar: Toolbar) {
        appCompatActivity?.setSupportActionBar(toolbar)
    }

    override fun addToFavourites(location: LocationResponse?) {
        // No Implementation needed
    }

    override fun removeFromFavourite(latLon: String) {
        favViewModel.removeFromFavourite(latLon)
    }
}