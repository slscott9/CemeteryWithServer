package com.example.cemeterywithserver.ui.cemeterydetail

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.cemeterywithserver.BaseFragment
import com.example.cemeterywithserver.R
import com.example.cemeterywithserver.databinding.FragmentCemeteryDetailBinding
import com.example.cemeterywithserver.ui.adapters.GraveListAdapter
import com.example.cemeterywithserver.ui.adapters.GraveListListener
import dagger.hilt.android.AndroidEntryPoint
import timber.log.Timber


/*
    onCreateView initialize and inflate layout

    onviewCreated ensures the views are inlfated safely reference them
 */
@AndroidEntryPoint
class CemeteryDetailFragment : BaseFragment(R.layout.fragment_cemetery_detail) {

    private lateinit var binding: FragmentCemeteryDetailBinding
    private val viewModel : CemeteryDetailViewModel by viewModels()
    private lateinit var graveListAdapter: GraveListAdapter
    private val args: CemeteryDetailFragmentArgs by navArgs()


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_cemetery_detail, container, false)
        binding.lifecycleOwner = viewLifecycleOwner
        binding.cemeteryDetailViewModel = viewModel

        viewModel.setCemeteryId(args.cemeteryId)
        Timber.i("cemetery id is ${args.cemeteryId}")


        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        graveListAdapter = GraveListAdapter(GraveListListener {

            findNavController().navigate(CemeteryDetailFragmentDirections.actionCemeteryDetailFragmentToGraveDetailFragment(it))

        })

        binding.graveRecyclerView.adapter = graveListAdapter

        binding.addGraveChip.setOnClickListener {
            findNavController().navigate(CemeteryDetailFragmentDirections.actionCemeteryDetailFragmentToAddEditGraveFragment(false))
        }


    }

}