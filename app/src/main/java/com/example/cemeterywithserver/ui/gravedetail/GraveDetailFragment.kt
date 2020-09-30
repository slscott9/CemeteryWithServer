package com.example.cemeterywithserver.ui.gravedetail

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.cemeterywithserver.BaseFragment
import com.example.cemeterywithserver.R
import com.example.cemeterywithserver.databinding.FragmentGraveDetailBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class GraveDetailFragment : BaseFragment(R.layout.fragment_grave_detail) {

    private val viewModel: GraveDetailViewModel by viewModels()
    private val args : GraveDetailFragmentArgs by navArgs()
    private lateinit var binding : FragmentGraveDetailBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_grave_detail, container, false)
        binding.lifecycleOwner = viewLifecycleOwner
        viewModel.setGraveId(args.graveId)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.editChip.setOnClickListener {
            findNavController().navigate(GraveDetailFragmentDirections.actionGraveDetailFragmentToAddEditGraveFragment(
                graveId = args.graveId,
                editGraveFlag = true
            ))
        }

        viewModel.graveSelected.observe(viewLifecycleOwner, Observer {
            it?.let {
                binding.grave = it
            }
        })
    }

}