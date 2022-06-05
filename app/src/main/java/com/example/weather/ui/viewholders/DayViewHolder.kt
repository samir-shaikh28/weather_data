package com.example.weather.ui.viewholders

import android.annotation.SuppressLint
import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.RequestManager
import com.example.weather.R
import com.example.weather.data.responses.DayResponse
import com.example.weather.data.responses.ForeCastDay
import com.example.weather.ui.adapters.GenericRecyclerAdapter
import com.example.weather.utils.HorizontalSpaceItemDecoration
import java.text.SimpleDateFormat
import java.util.*

class DayViewHolder(val days: List<ForeCastDay>?, val requestManager: RequestManager): AbstractViewHolder() {

    var isViewInitialized = false

    override fun getLayoutIdentifier(): Int {
        return R.layout.layout_day_forecast
    }

    companion object {

        @SuppressLint("SimpleDateFormat")
        @JvmStatic
        @BindingAdapter("setData")
        fun setData(recyclerView: RecyclerView, model: DayViewHolder) {

            val currentHour: String =  SimpleDateFormat("HH").format(Date())
            val context = recyclerView.context
            if (!model.isViewInitialized) {
                val listOfDays = mutableListOf<AbstractViewHolder>()
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

                model.days?.forEach {
                    listOfDays.add(DaysItemVH(it.day, it.date, model.requestManager))
                }
                val scoreListAdapter = GenericRecyclerAdapter(listOfDays)
                recyclerView.adapter = scoreListAdapter
                scoreListAdapter.notifyDataSetChanged()
                recyclerView.scrollToPosition(currentHour.toInt())
            }

        }
    }
}