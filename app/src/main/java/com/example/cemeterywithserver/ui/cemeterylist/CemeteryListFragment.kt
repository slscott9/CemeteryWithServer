package com.example.cemeterywithserver.ui.cemeterylist

import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.provider.ContactsContract
import android.view.*
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.NavOptions
import androidx.navigation.fragment.findNavController
import com.example.cemeterywithserver.BaseFragment
import com.example.cemeterywithserver.R
import com.example.cemeterywithserver.databinding.FragmentCemeteryListBinding
import com.example.cemeterywithserver.other.Constants.KEY_LOGGED_IN_EMAIL
import com.example.cemeterywithserver.other.Constants.KEY_PASSWORD
import com.example.cemeterywithserver.other.Constants.NO_EMAIL
import com.example.cemeterywithserver.other.Constants.NO_PASSWORD
import com.example.cemeterywithserver.other.Status
import com.example.cemeterywithserver.ui.adapters.CemeteryListAdapter
import com.example.cemeterywithserver.ui.adapters.CemeteryListener
import com.example.cemeterywithserver.ui.cemeterydetail.CemeteryDetailFragment
import com.example.cemeterywithserver.ui.cemeterydetail.CemeteryDetailViewModel
import dagger.hilt.android.AndroidEntryPoint
import timber.log.Timber
import javax.inject.Inject

@AndroidEntryPoint
class CemeteryListFragment : BaseFragment(R.layout.fragment_cemetery_list) {

    @Inject
    lateinit var sharedPref: SharedPreferences

    private val viewModel: CemeteryListViewModel by viewModels()
    private lateinit var cemeteryListAdapter : CemeteryListAdapter
    private lateinit var binding: FragmentCemeteryListBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_cemetery_list, container, false)
        binding.lifecycleOwner = viewLifecycleOwner
        setHasOptionsMenu(true)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupRecyclerView()
        subscribeToObservers()
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.menu_cemeteries, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        when(item.itemId){
            R.id.miLogout -> logout() //item logout is clicked pop up to cemetery list fragment redirect to login fragment
        }
        return super.onOptionsItemSelected(item)

    }

    private fun setupRecyclerView() {
         cemeteryListAdapter = CemeteryListAdapter(CemeteryListener {

             startActivity(
                 Intent(requireActivity(), CemeteryDetailFragment::class.java).apply {
                     putExtra(CemeteryDetailViewModel.CEMETERY_ID, it)
                 }
             )
         })

        binding.cemeteryListRecyclerView.adapter = cemeteryListAdapter
    }

    private fun logout() {
        //Set shared prefs to no email and no password so login fragment knows if user is logged in or not
        sharedPref.edit().putString(KEY_LOGGED_IN_EMAIL, NO_EMAIL).apply() //no email and no password in shared prefs when user logs out. Redirect to login fragment
        sharedPref.edit().putString(KEY_PASSWORD, NO_PASSWORD).apply()

        //pop everything except login fragment
        val navOptions = NavOptions.Builder()
            .setPopUpTo(R.id.cemeteryListFragment, true)
            .build()
        findNavController().navigate(
            CemeteryListFragmentDirections.actionCemeteryListFragmentToLoginFragment(),
            navOptions
        )
    }

    //use event to only show the snackbar once
    private fun subscribeToObservers() {
        viewModel.allCemteries.observe(viewLifecycleOwner, Observer {
            it?.let { event ->

                val result = event.peekContent() //always returns content of event does not consume it

                when(result.status){
                    Status.SUCCESS -> {
                        cemeteryListAdapter.submitList(result.data!!)
                    }
                    Status.ERROR -> {
                            //consume event here when something went wrong
                        //let block is executed only if an error occurs
                        event.getContentIfNotHandled()?.let { errorResource ->
                            errorResource.message?.let {message ->
                                showSnackBar(message)
                            }
                        }
                        result.data?.let {cemList ->
                            cemeteryListAdapter.submitList(cemList)
                        }

                        Timber.i("Error status in cemetery list fragment")

                    }
                    Status.LOADING -> {
                        result.data?.let {cemList ->
                            cemeteryListAdapter.submitList(cemList)
                        }
                        Timber.i("Loading status in cemetery list fragment")
                    }
                }
            }
        })
    }
}