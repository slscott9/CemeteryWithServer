package com.example.cemeterywithserver.ui.cemeterydetail

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.viewModels
import com.example.cemeterywithserver.BaseFragment
import com.example.cemeterywithserver.R
import com.example.cemeterywithserver.databinding.FragmentCemeteryDetailBinding
import dagger.hilt.android.AndroidEntryPoint


/*
    onCreateView initialize and inflate layout

    onviewCreated ensures the views are inlfated safely reference them
 */
@AndroidEntryPoint
class CemeteryDetailFragment : BaseFragment(R.layout.fragment_cemetery_detail) {

    private lateinit var binding: FragmentCemeteryDetailBinding
    private val viewModel : CemeteryDetailViewModel by viewModels()


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_cemetery_detail, container, false)
        binding.lifecycleOwner = viewLifecycleOwner
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


    }

}