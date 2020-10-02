package com.example.cemeterywithserver.ui.addeditgrave

import android.app.DatePickerDialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.NavOptions
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.cemeterywithserver.BaseFragment
import com.example.cemeterywithserver.R
import com.example.cemeterywithserver.data.entitites.Grave
import com.example.cemeterywithserver.databinding.FragmentAddEditGraveBinding
import dagger.hilt.android.AndroidEntryPoint
import timber.log.Timber
import java.util.*

@AndroidEntryPoint
class AddEditGraveFragment : BaseFragment(R.layout.fragment_add_edit_grave) {

    private lateinit var binding : FragmentAddEditGraveBinding
    private val viewModel : AddEditGraveViewModel by viewModels()
    private val args : AddEditGraveFragmentArgs by navArgs()

    private lateinit var bornDateListener: DatePickerDialog.OnDateSetListener
    private lateinit var deathDateListener: DatePickerDialog.OnDateSetListener
    private lateinit var marriedDateListener: DatePickerDialog.OnDateSetListener
    private  var graveToEdit : Grave ? = null
    private var cemeteryID: String ? = null

    private var cal = Calendar.getInstance()
    var year = cal.get(Calendar.YEAR)
    var month = cal.get(Calendar.MONTH)
    var day = cal.get(Calendar.DAY_OF_MONTH)

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_add_edit_grave, container, false)
        binding.lifecycleOwner = viewLifecycleOwner

        if(args.editGraveFlag){
            viewModel.setGraveId(args.graveId!!)
        }

        viewModel.graveToEdit.observe(viewLifecycleOwner, Observer {
            it?.let {
                graveToEdit = it //will not be null if we are updating an existing grave
            }
        })
        binding.viewModel = viewModel

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        bornDateListener = DatePickerDialog.OnDateSetListener { view, year, month, dayOfMonth ->
            val date: String = "$month / $dayOfMonth / $year"
            binding.bornEt.setText(date) //use this instead of .text since edit text expects exitable instead of string
        }

        deathDateListener = DatePickerDialog.OnDateSetListener { view, year, month, dayOfMonth ->
            val date: String = "$month / $dayOfMonth / $year"
            binding.deathYearEt.setText(date) //use this instead of .text since edit text expects exitable instead of string
        }

        marriedDateListener = DatePickerDialog.OnDateSetListener { view, year, month, dayOfMonth ->
            val date: String = "$month / $dayOfMonth / $year"
            binding.marriageYearEt.setText(date) //use this instead of .text since edit text expects exitable instead of string
        }

        binding.marriageYearInputLayout.setStartIconOnClickListener {
            showDatePicker(marriedDateListener)
        }
        binding.deathYearInputLayout.setStartIconOnClickListener {
            showDatePicker(deathDateListener)
        }
        binding.birthYearInputLayout.setStartIconOnClickListener {
            showDatePicker(bornDateListener)
        }

        binding.createGraveFAB.setOnClickListener {
            if(
                binding.firstNameEt.text.isNullOrEmpty() ||
                binding.lastNameet.text.isNullOrEmpty() ||
                binding.bornEt.text.isNullOrEmpty() ||
                binding.deathYearEt.text.isNullOrEmpty() ||
                binding.marriageYearEt.text.isNullOrEmpty() ||
                binding.commentEt.text.isNullOrEmpty() ||
                binding.graveNumEt.text.isNullOrEmpty()){
                Toast.makeText(requireActivity(), "Please enter all fields", Toast.LENGTH_SHORT).show()
            }else{
                val grave =
                    Grave(
                        graveId =  args.graveId ?: UUID.randomUUID().toString(), //if grave id is null then user is creating a new grave, generate uuid else user is editing existing grave
                        firstName = binding.firstNameEt.text.toString(),
                        lastName = binding.lastNameet.text.toString(),
                        birthDate = binding.bornEt.text.toString(),
                        deathDate = binding.bornEt.text.toString(),
                        marriageYear = binding.marriageYearEt.text.toString(),
                        comment = binding.commentEt.text.toString(),
                        graveNumber = binding.graveNumEt.text.toString(),
                        cemeteryId = args.cemeteryId ?: graveToEdit!!.cemeteryId, //cemId null? then user is creating new grave else user is editing existing grave

                    //if cemeteryId is  null we are updating an existing grave
                    //if its not null then we are adding a new grave
                    )
                viewModel.addGrave(grave)
                redirectToCemList()
            }
        }
    }

    private fun redirectToCemList() {
        val navOptions = NavOptions.Builder()
            .setPopUpTo(R.id.cemeteryDetailFragment, true) //pops everything off stack, current stack has only cemeteryDetailFragment
            .build()
        findNavController().navigate(
            AddEditGraveFragmentDirections.actionAddEditGraveFragmentToCemeteryDetailFragment(
                graveToEdit?.cemeteryId ?: args.cemeteryId), navOptions
        )
    }


    private fun showDatePicker(dateListener : DatePickerDialog.OnDateSetListener) {
        val dialog = DatePickerDialog(requireActivity(), android.R.style.Theme_Holo_Dialog, dateListener, year, month, day)
        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog.show()
    }





}