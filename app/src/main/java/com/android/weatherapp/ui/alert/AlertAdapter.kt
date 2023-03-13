package com.android.weatherapp.ui.alert

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.android.weatherapp.databinding.AlertItemBinding

// TODO 1#7 Create Adapter of alert
class AlertAdapter(val context: Context, val onDeleteClicked: (model: AlertModel) -> Unit) :
    RecyclerView.Adapter<AlertAdapter.FavoriteCityViewHolder>() {

    inner class FavoriteCityViewHolder(val itemBinding: AlertItemBinding) :
        RecyclerView.ViewHolder(itemBinding.root)

    private val differCallback = object : DiffUtil.ItemCallback<AlertModel>() {
        override fun areItemsTheSame(oldItem: AlertModel, newItem: AlertModel): Boolean {
            return oldItem == newItem
        }

        override fun areContentsTheSame(oldItem: AlertModel, newItem: AlertModel): Boolean {
            return oldItem.id == newItem.id
        }
    }

    val differ = AsyncListDiffer(this, differCallback)

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): AlertAdapter.FavoriteCityViewHolder {
        val itemBinding =
            AlertItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return FavoriteCityViewHolder(itemBinding)
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(
        holder: AlertAdapter.FavoriteCityViewHolder,
        position: Int
    ) {
        val alertModel = differ.currentList[position]

        // TODO convert timestamp to Human readable date and time
        holder.itemBinding.textViewFrom.text = "${ dayConverterToString(alertModel.startDate?:0,context) }  ${ timeConverterToString(alertModel.startDate?:0,context) }  "
        holder.itemBinding.textViewTo.text = "${ dayConverterToString(alertModel.endDate?:0,context) }  ${ timeConverterToString(alertModel.endTime?:0,context) }  "

        // TODO delete Action
        holder.itemBinding.imageButtonDelete.setOnClickListener {
            onDeleteClicked(alertModel)
        }
    }

    override fun getItemCount(): Int {
        return differ.currentList.size
    }

}