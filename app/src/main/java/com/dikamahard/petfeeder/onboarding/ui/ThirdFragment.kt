package com.dikamahard.petfeeder.onboarding.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.dikamahard.petfeeder.R
import com.dikamahard.petfeeder.databinding.FragmentFirstBinding
import com.dikamahard.petfeeder.databinding.FragmentThirdBinding
import com.dikamahard.petfeeder.helper.StatePreferences
import com.dikamahard.petfeeder.helper.dataStore
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class ThirdFragment : Fragment() {

    //private lateinit var binding: FragmentThirdBinding
    //private lateinit var dataStore: DataStore<Preferences>

    private lateinit var statePreferences: StatePreferences


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        statePreferences = StatePreferences.getInstance(requireContext().dataStore)

        val view =  inflater.inflate(R.layout.fragment_third, container, false)

        view.findViewById<TextView>(R.id.finish).setOnClickListener {
            lifecycleScope.launch(Dispatchers.Default) {
                statePreferences.saveState("onboarding_state_key", true)

                withContext(Dispatchers.Main){
                    findNavController().navigate(R.id.action_viewPagerFragment_to_roleFragment)
                }
            }
        }
        // Inflate the layout for this fragment
        return view
    }

}