package com.example.cemeterywithserver.ui.gravedetail

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.cemeterywithserver.BaseFragment
import com.example.cemeterywithserver.R
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class GraveDetailFragment : BaseFragment(R.layout.fragment_grave_detail) {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_grave_detail, container, false)
    }

}