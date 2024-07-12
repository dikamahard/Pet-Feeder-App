package com.dikamahard.petfeeder

import android.os.Bundle
import android.os.Handler
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.dikamahard.petfeeder.helper.StatePreferences
import com.dikamahard.petfeeder.helper.dataStore
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext


class SplashFragment : Fragment() {


    private lateinit var statePreferences: StatePreferences

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        statePreferences = StatePreferences.getInstance(requireContext().dataStore)


        lifecycleScope.launch(Dispatchers.Default) {
            delay(3000)  // Use delay within coroutine to handle timing
            navigateBasedOnState()
        }

//        Handler().postDelayed({
//            lifecycleScope.launch {
//                if (onBoardingIsDone() && roleChosenIsDone()) {
//                    findNavController().navigate(R.id.action_roleFragment_to_homeFragment)
//                }else if (onBoardingIsDone()){
//                    findNavController().navigate(R.id.action_splashFragment_to_roleFragment)
//                }else {
//                    findNavController().navigate(R.id.action_splashFragment_to_viewPagerFragment)
//                }
//            }
//        }, 3000)
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_splash, container, false)
    }


    private suspend fun navigateBasedOnState() {
//        if (onBoardingIsDone() && roleChosenIsDone()) {
//            withContext(Dispatchers.Main) {
//                findNavController().navigate(R.id.action_roleFragment_to_homeFragment)
//            }
//        } else if (onBoardingIsDone()) {
//            withContext(Dispatchers.Main) {
//                findNavController().navigate(R.id.action_splashFragment_to_roleFragment)
//            }
//        } else {
//            withContext(Dispatchers.Main) {
//                findNavController().navigate(R.id.action_splashFragment_to_viewPagerFragment)
//            }
//        }
        try {
            when {
                onBoardingIsDone() && roleChosenIsDone() -> findNavController().navigate(R.id.action_splashFragment_to_homeFragment)
                onBoardingIsDone() -> findNavController().navigate(R.id.action_splashFragment_to_roleFragment)
                else -> findNavController().navigate(R.id.action_splashFragment_to_viewPagerFragment)
            }
        } catch (e: Exception) {
            Log.e("SplashFragment", "Error navigating: ${e.message}")
            // Handle error or navigate to an error-specific screen
        }
    }

    private suspend fun onBoardingIsDone(): Boolean{
        return statePreferences.getState("onboarding_state_key")
    }

    // TODO:check role chosen
    private suspend fun roleChosenIsDone(): Boolean{
        return (statePreferences.getState("role_state_key"))
    }

}