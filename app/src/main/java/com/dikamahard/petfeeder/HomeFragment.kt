package com.dikamahard.petfeeder

import androidx.fragment.app.viewModels
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.lifecycleScope
import com.dikamahard.petfeeder.databinding.FragmentHomeBinding
import com.dikamahard.petfeeder.databinding.FragmentRoleBinding
import com.dikamahard.petfeeder.helper.StatePreferences
import com.dikamahard.petfeeder.helper.dataStore
import kotlinx.coroutines.launch

class HomeFragment : Fragment() {

    companion object {
        fun newInstance() = HomeFragment()
    }

    private val viewModel: HomeViewModel by viewModels()
    private lateinit var binding: FragmentHomeBinding
    private lateinit var statePreferences: StatePreferences


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // TODO: Use the ViewModel
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        statePreferences = StatePreferences.getInstance(requireContext().dataStore)


        binding.btnTes.setOnClickListener {
            lifecycleScope.launch {
                val role = statePreferences.getRole("role_chosen_key")

                Log.d("TAG", "role: $role")
                Log.d("TAG", "role state: ${statePreferences.getState("role_state_key")}")
                Log.d("TAG", "onboarding state: ${statePreferences.getState("onboarding_state_key")}")


            }
        }

    }
}