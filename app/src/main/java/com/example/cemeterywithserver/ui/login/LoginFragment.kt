package com.example.cemeterywithserver.ui.login

import android.content.SharedPreferences
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
import com.example.cemeterywithserver.data.remote.BasicAuthInterceptor
import com.example.cemeterywithserver.databinding.FragmentLoginBinding
import com.example.cemeterywithserver.other.Constants.KEY_LOGGED_IN_EMAIL
import com.example.cemeterywithserver.other.Constants.KEY_PASSWORD
import com.example.cemeterywithserver.other.Status
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject
import kotlin.coroutines.Continuation

@AndroidEntryPoint
class LoginFragment : BaseFragment(R.layout.fragment_login) {


    private lateinit var binding: FragmentLoginBinding
    private val viewModel: LoginViewModel by viewModels()

    @Inject
     lateinit var sharedPref: SharedPreferences

    @Inject
    lateinit var basicAuthInterceptor: BasicAuthInterceptor

    private var currentEmail: String? = null
    private var currentPassword: String? = null


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_login, container,false)
        binding.lifecycleOwner = this

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.tvRegister.setOnClickListener {
            findNavController().navigate(R.id.action_loginFragment_to_registerFragment)
        }

        binding.btnLogin.setOnClickListener {
            val email = binding.etUserName.text.toString()
            val password = binding.etPassword.text.toString()

            currentEmail = email
            currentPassword = password
            viewModel.login(email, password)
        }

        viewModel.loginStatus.observe(viewLifecycleOwner, Observer {
            it?.let {
                when(it.status){
                    Status.SUCCESS -> {
                        binding.loginProgressBar.visibility = View.GONE
                        showSnackBar(it.data ?: "Successfully logged in")
                        sharedPref.edit().putString(KEY_LOGGED_IN_EMAIL, currentEmail).apply()
                        sharedPref.edit().putString(KEY_PASSWORD, currentPassword).apply()

                        authenticateAPI(currentEmail?: "" , currentPassword ?: "")
                        redirectLogin()
                    }
                    Status.ERROR -> {
                        binding.loginProgressBar.visibility = View.GONE
                        showSnackBar(it.message ?: "Unknown error occurred")
                    }
                    Status.LOADING -> {
                        binding.loginProgressBar.visibility = View.VISIBLE
                    }
                }
            }
        })


    }

    //function is called we pop loginFragment off of backstack killing it and redirect to cemetery list fragment
    //TODO replace register fragment with this code
    private fun redirectLogin() {
        val navOptions = NavOptions.Builder()
            .setPopUpTo(R.id.loginFragment, true) //kills login fragment so when back button is pressed from cemetery list we do not go back to login fragment
            .build()
        findNavController().navigate(
            LoginFragmentDirections.actionLoginFragmentToCemeteryListFragment(), navOptions
        )
    }

    private fun authenticateAPI(email: String, password: String) {
        basicAuthInterceptor.email = email
        basicAuthInterceptor.password = password
    }

}