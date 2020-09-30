package com.example.cemeterywithserver.ui.addeditcemetery

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.viewModels
import androidx.navigation.NavArgs
import androidx.navigation.fragment.navArgs
import com.example.cemeterywithserver.BaseFragment
import com.example.cemeterywithserver.R
import com.example.cemeterywithserver.databinding.FragmentAddEditCemBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class AddEditCemFragment : BaseFragment(R.layout.fragment_add_edit_cem) {

    private lateinit var binding : FragmentAddEditCemBinding
    private val viewModel: AddEditCemViewModel by viewModels()
    private val args: AddEditCemFragmentArgs by navArgs()



    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_add_edit_cem, container, false)
        binding.lifecycleOwner = viewLifecycleOwner

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        if(args.editCemFlag){
            //call view model method to populate cemetery field
        }
    }

}