package com.example.cemeterywithserver.ui.login

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.NavOptions
import androidx.navigation.fragment.findNavController
import com.example.cemeterywithserver.BaseFragment
import com.example.cemeterywithserver.R
import com.example.cemeterywithserver.databinding.FragmentRegisterBinding
import com.example.cemeterywithserver.other.Status
import dagger.hilt.android.AndroidEntryPoint


/*
    onCreateView should inflate layout but initialize views and listeners in onViewCreated

    Because sometimes view is not properly initialized. So always use findViewById in onViewCreated(when view is fully created) and it also passes the view as parameter.
    onViewCreated is a make sure that view is fully created.
 */
@AndroidEntryPoint
class RegisterFragment : BaseFragment(R.layout.fragment_register) {

    private val viewModel: RegisterViewModel by viewModels()

    private lateinit var binding: FragmentRegisterBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment

        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_register, container, false)
        binding.lifecycleOwner = this
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.btnRegister.setOnClickListener {
            viewModel.register(
                email = binding.etUserName.text.toString(),
                password = binding.etPassword.text.toString(),
                repeatedPassword = binding.etConfirmPassword.text.toString()
            )
        }

        viewModel.registerStatus.observe(viewLifecycleOwner, Observer {
            it?.let {
               when(it.status){
                   Status.SUCCESS -> {
                       binding.registerProgressBar.visibility = View.GONE
                       showSnackBar(it.data ?: "Successfully registered!")
                        redirectRegister()
                   }
                   Status.ERROR -> {
                       binding.registerProgressBar.visibility = View.GONE
                       showSnackBar(it.message ?: "An unknown error occurred")
                   }
                   Status.LOADING -> {
                       binding.registerProgressBar.visibility = View.VISIBLE
                   }
               }
            }
        })


    }

    private fun redirectRegister() {
        val navOptions = NavOptions.Builder()
            .setPopUpTo(R.id.registerFragment, true) //kills login fragment so when back button is pressed from cemetery list we do not go back to login fragment
            .build()
        findNavController().navigate(
            RegisterFragmentDirections.actionRegisterFragmentToLoginFragment(), navOptions
        )
    }



}