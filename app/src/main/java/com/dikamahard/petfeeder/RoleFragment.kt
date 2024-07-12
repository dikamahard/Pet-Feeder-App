package com.dikamahard.petfeeder

import androidx.fragment.app.viewModels
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RadioButton
import android.widget.Toast
import androidx.datastore.preferences.preferencesDataStore
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.dikamahard.petfeeder.databinding.FragmentRoleBinding
import com.dikamahard.petfeeder.helper.StatePreferences
import com.dikamahard.petfeeder.helper.dataStore
import kotlinx.coroutines.launch

class RoleFragment : Fragment() {


    private val viewModel: RoleViewModel by viewModels()
    private lateinit var binding: FragmentRoleBinding
    private lateinit var statePreferences: StatePreferences


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentRoleBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        statePreferences = StatePreferences.getInstance(requireContext().dataStore)


        binding.btnStart.setOnClickListener {
            val selectedId = binding.radioGroup.checkedRadioButtonId

            lifecycleScope.launch {
                Log.d("TAG", "onboarding: ${statePreferences.getState("onboarding_state_key")}")
            }

            if (selectedId == -1) {
                Toast.makeText(requireContext(), "Please choose 1", Toast.LENGTH_SHORT).show()
            }else {
                val radioButton = view.findViewById<RadioButton>(selectedId)
                val radioButtonText = radioButton.text.toString()
                lifecycleScope.launch {
                    statePreferences.saveState("role_state_key", true)
                    statePreferences.saveRole("role_chosen_key", radioButtonText)
                }
                findNavController().navigate(R.id.action_roleFragment_to_homeFragment)
            }
        }


    }

}