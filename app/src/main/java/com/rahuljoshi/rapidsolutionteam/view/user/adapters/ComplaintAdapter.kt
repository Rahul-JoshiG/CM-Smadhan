package com.rahuljoshi.rapidsolutionteam.view.user.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.rahuljoshi.rapidsolutionteam.databinding.MyComplaintLayoutBinding
import com.rahuljoshi.rapidsolutionteam.interfaces.OnShowComplaint

class ComplaintAdapter(
    private val complaints: List<Map<String, Any>>,
    private val listener: OnShowComplaint
) : RecyclerView.Adapter<ComplaintAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = MyComplaintLayoutBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val currentComplaint = complaints[position]
        holder.bind(currentComplaint)
    }

    override fun getItemCount(): Int = complaints.size

    inner class ViewHolder(private val binding: MyComplaintLayoutBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(complaint: Map<String, Any>) {
            binding.complaintName.text = complaint["title"].toString()
            binding.complaintStatus.text = complaint["type"].toString()
            binding.complaintDepartment.text = complaint["department"].toString()

            // Handle item click
            itemView.setOnClickListener {
                listener.showComplaint(complaint)
            }
        }
    }
}
