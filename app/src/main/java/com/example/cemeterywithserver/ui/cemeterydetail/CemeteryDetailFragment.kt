package com.example.cemeterywithserver.ui.cemeterydetail

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.cemeterywithserver.BaseFragment
import com.example.cemeterywithserver.R
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class CemeteryDetailFragment : BaseFragment(R.layout.fragment_cemetery_detail) {


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_cemetery_detail, container, false)
    }

}