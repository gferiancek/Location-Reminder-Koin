package com.example.neoradar.adapters

import android.view.View
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.request.RequestOptions
import com.example.neoradar.R
import com.example.neoradar.domain.ImageOfTheDay
import com.example.neoradar.domain.Neo
import com.example.neoradar.viewmodel.NeoApiStatus

@BindingAdapter("setHazardIcon")
fun ImageView.setListHazardousIcon(item: Neo?) {
    item?.let {
        if (item.isPotentiallyHazardous) {
            setImageResource(
                when (id) {
                    R.id.neo_list_hazardous_icon -> R.drawable.ic_list_potentially_hazardous
                    else -> R.drawable.ic_detail_potentially_hazardous
                }
            )
        } else {
            setImageResource(
                when (id) {
                    R.id.neo_list_hazardous_icon -> R.drawable.ic_list_not_hazardous
                    else -> R.drawable.ic_detail_not_hazardous
                }
            )
        }
    }
}

@BindingAdapter("distanceFromEarth")
fun TextView.distanceFromEarth(item: Neo?) {
    item?.let {
        text = resources.getString(R.string.neo_list_distance, item.distanceFromEarth)
    }
}

@BindingAdapter("setHazardtext")
fun TextView.setHazardText(item: Neo?) {
    item?.let {
        text = when (item.isPotentiallyHazardous) {
            true -> resources.getString(R.string.is_potentially_hazardous)
            false -> resources.getString(R.string.not_hazardous)
        }
    }
}

@BindingAdapter("addAu")
fun TextView.addAu(double: Double?) {
    double?.let {
        text = resources.getString(R.string.add_au, double)
    }
}

@BindingAdapter("addKm")
fun TextView.addKm(double: Double?) {
    double?.let {
        text = resources.getString(R.string.add_km, double)
    }
}

@BindingAdapter("addKms")
fun TextView.addKms(double: Double?) {
    double?.let {
        text = resources.getString(R.string.add_kms, double)
    }
}

@BindingAdapter("neoListData")
fun RecyclerView.bindNeoList(neoList: List<Neo>?) {
    neoList?.let {
        val adapter = adapter as NeoAdapter
        adapter.submitList(neoList)
    }
}

@BindingAdapter("imageOfTheDay")
fun ImageView.loadImageOfTheDay(item: ImageOfTheDay?) {
    item?.let {
        val options = RequestOptions()
            .error(R.drawable.ic_placeholder_of_the_day)
            .diskCacheStrategy(DiskCacheStrategy.ALL)
            .centerCrop()

        Glide.with(context)
            .load(item.url)
            .apply(options)
            .into(this)
    }
}

@BindingAdapter("uiStatus")
fun setUiStatus(view: View, apiStatus: NeoApiStatus?) {
    apiStatus?.let {
        view.visibility = when (view) {
            is ConstraintLayout -> if (apiStatus == NeoApiStatus.DONE) View.VISIBLE else View.GONE
            is ProgressBar -> if (apiStatus == NeoApiStatus.LOADING) View.VISIBLE else View.GONE
            else -> if (apiStatus == NeoApiStatus.ERROR) View.VISIBLE else View.GONE
        }
    }
}

@BindingAdapter("setContentDescription")
fun ImageView.setContentDescription(item: Neo?) {
    item?.let {
        contentDescription =  when (item.isPotentiallyHazardous) {
            true -> resources.getString(R.string.potentially_hazardous_content_description)
            false -> resources.getString(R.string.not_hazardous_content_description)
        }
    }
}