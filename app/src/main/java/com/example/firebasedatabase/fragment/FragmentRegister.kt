package com.example.firebasedatabase.fragment

import android.content.Context
import android.content.SharedPreferences
import android.content.SharedPreferences.Editor
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.firebasedatabase.R
import com.example.firebasedatabase.constants.DATABASE_NAME
import com.example.firebasedatabase.constants.PERSON_INFO
import com.example.firebasedatabase.dataClasses.PersonInfo
import com.example.firebasedatabase.databinding.RegisterFragmentBinding

class FragmentRegister : Fragment(R.layout.register_fragment) {

    private lateinit var binding: RegisterFragmentBinding
    private lateinit var sharedPreferences: SharedPreferences
    private lateinit var preferencesEditor: Editor

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        checkSharedPreferences()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = RegisterFragmentBinding.bind(view)

        toRegister()
    }

    private fun toRegister() {
        binding.apply {
            registerButton.setOnClickListener {
                if (!isFelledEmpty()) {
                    val personInfo = PersonInfo(
                        nameEdit.text.toString(),
                        lastNameEdit.text.toString(),
                        surNameEdit.text.toString()
                    )
                    preferencesEditor.putString(PERSON_INFO, personInfo.name)
                    preferencesEditor.commit()
                    findNavController().navigate(R.id.openChatPage)
                }
            }
        }
    }

    private fun isFelledEmpty(): Boolean {
        binding.apply {
            if (nameEdit.text.isNullOrEmpty()) nameEdit.error = "Fill the filled"
            if (lastNameEdit.text.isNullOrEmpty()) lastNameEdit.error = "Fill the filled"
            if (surNameEdit.text.isNullOrEmpty()) surNameEdit.error = "Fill the filled"
        }
        return binding.nameEdit.text.isNullOrEmpty()
                || binding.lastNameEdit.text.isNullOrEmpty() ||
                binding.surNameEdit.text.isNullOrEmpty()
    }

    private fun checkSharedPreferences() {
        sharedPreferences =
            requireContext().getSharedPreferences(DATABASE_NAME, Context.MODE_PRIVATE)
        preferencesEditor = sharedPreferences.edit()
        if (sharedPreferences.contains(PERSON_INFO)) {
            findNavController().apply {
                navigate(R.id.openChatPage)
            }
        }
    }
}