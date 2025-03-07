package com.rahuljoshi.rapidsolutionteam.view.user.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.rahuljoshi.rapidsolutionteam.databinding.FragmentAIChatbotBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class AiChatbotFragment : Fragment() {
    private lateinit var mBinding : FragmentAIChatbotBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        mBinding = FragmentAIChatbotBinding.inflate(inflater, container, false)
        return mBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

    }

    companion object {
        private const val TAG = "AIChatbotFragment"
    }
}