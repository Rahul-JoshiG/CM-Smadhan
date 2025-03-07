package com.rahuljoshi.rapidsolutionteam.view.user.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.rahuljoshi.rapidsolutionteam.interfaces.TeamsInterface
import com.rahuljoshi.rapidsolutionteam.data.SolutionTeam
import com.rahuljoshi.rapidsolutionteam.databinding.SolutionTeamLayoutBinding

class SolutionTeamAdapter(private val teamList: List<SolutionTeam>, val listener: TeamsInterface) :
    RecyclerView.Adapter<SolutionTeamAdapter.ViewHolder>(){

    private lateinit var mBinding: SolutionTeamLayoutBinding

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        mBinding =
            SolutionTeamLayoutBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(mBinding.root)
    }

    override fun getItemCount(): Int {
        return teamList.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val current = teamList[position]
        holder.bind(current)
    }

    inner class ViewHolder(root: View) : RecyclerView.ViewHolder(root) {

        fun bind(current: SolutionTeam) {
            mBinding.teamName.text = current.name

            mBinding.teamName.setOnClickListener{
                listener.onTeamClicked(current.name)
            }
        }
    }

}