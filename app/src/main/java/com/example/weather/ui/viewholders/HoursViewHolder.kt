package com.example.weather.ui.viewholders

import android.annotation.SuppressLint
import android.util.Log
import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.RequestManager
import com.example.weather.R
import com.example.weather.data.responses.HourResponse
import com.example.weather.ui.adapters.GenericRecyclerAdapter
import com.example.weather.utils.HorizontalSpaceItemDecoration
import java.text.SimpleDateFormat
import java.util.*

class HoursViewHolder(private val hours: List<HourResponse>?,
                      val requestManager: RequestManager) : AbstractViewHolder
    () {

    var isViewInitialized = false

    override fun getLayoutIdentifier(): Int {
        return R.layout.layout_hour_forecast
    }

    companion object {

        @SuppressLint("SimpleDateFormat")
        @JvmStatic
        @BindingAdapter("setData")
        fun setData(recyclerView: RecyclerView, model: HoursViewHolder) {

            val currentHour: String = SimpleDateFormat("HH").format(Date())
            val context = recyclerView.context
            if (!model.isViewInitialized) {
                val listOfHours = mutableListOf<AbstractViewHolder>()
                recyclerView.layoutManager =
                    LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
                recyclerView.addItemDecoration(
                    HorizontalSpaceItemDecoration(
                        context.resources.getDimension(
                            R.dimen.padding_12dp
                        ).toInt()
                    )
                )
                model.isViewInitialized = true

                model.hours?.forEach {
                    listOfHours.add(HourItemVH(it, model.requestManager))
                }
                val scoreListAdapter = GenericRecyclerAdapter(listOfHours)
                recyclerView.adapter = scoreListAdapter
                scoreListAdapter.notifyDataSetChanged()
                recyclerView.scrollToPosition(currentHour.toInt())
            }
        }
    }
}