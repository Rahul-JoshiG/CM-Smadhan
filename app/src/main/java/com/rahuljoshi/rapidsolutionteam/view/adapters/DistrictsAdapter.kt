package com.rahuljoshi.rapidsolutionteam.view.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.rahuljoshi.rapidsolutionteam.data.Districts
import com.rahuljoshi.rapidsolutionteam.databinding.DistrictLayoutBinding
import com.rahuljoshi.rapidsolutionteam.interfaces.TeamsInterface

class DistrictsAdapter(
    private val districts: List<Districts>,
    private val listener: TeamsInterface
) : RecyclerView.Adapter<DistrictsAdapter.ViewHolder>() {

    private lateinit var mBinding : DistrictLayoutBinding

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        mBinding = DistrictLayoutBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(mBinding.root)
    }

    override fun getItemCount(): Int {
        return districts.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val current = districts[position]
        holder.bind(current)
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        fun bind(current: Districts) {
            mBinding.districtName.text = current.name

            mBinding.districtName.setOnClickListener{
                listener.onTeamClicked(current.name)
            }
        }
    }
}
