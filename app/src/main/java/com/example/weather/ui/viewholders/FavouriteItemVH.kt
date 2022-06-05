package com.example.weather.ui.viewholders

import android.view.View
import androidx.appcompat.widget.AppCompatCheckBox
import androidx.databinding.BindingAdapter
import androidx.databinding.ObservableBoolean
import com.example.weather.R
import com.example.weather.data.db.entity.FavouriteEntity
import com.example.weather.listener.OnBookMarkClickListener

class FavouriteItemVH(val favouriteEntity: FavouriteEntity, val listener: OnBookMarkClickListener) :
    AbstractViewHolder() {

    var isBookMarkObserver: ObservableBoolean = ObservableBoolean(true)

    fun onBookMarkClick(v: View) {
        (v as? AppCompatCheckBox)?.apply {
            isBookMarkObserver.set(isChecked)
        }
        listener.removeFromFavourite(favouriteEntity.latLon)
    }

    override fun getLayoutIdentifier(): Int {
        return R.layout.layout_item_favourite
    }

    companion object {
        @JvmStatic
        @BindingAdapter("setVectorSelector")
        fun mediaItemImage(checkbox: AppCompatCheckBox, model: FavouriteItemVH) {
            checkbox.setButtonDrawable(R.drawable.bg_bookmark_inset)
        }
    }
}