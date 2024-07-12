package com.dikamahard.petfeeder.onboarding.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.viewpager2.widget.ViewPager2
import com.dikamahard.petfeeder.R
import com.dikamahard.petfeeder.databinding.FragmentFirstBinding
import com.dikamahard.petfeeder.databinding.FragmentSecondBinding


class SecondFragment : Fragment() {

    //private lateinit var binding: FragmentSecondBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val view =  inflater.inflate(R.layout.fragment_second, container, false)

        val viewPager = activity?.findViewById<ViewPager2>(R.id.viewPager)

        view?.findViewById<TextView>(R.id.next)?.setOnClickListener {
            viewPager?.currentItem = 2
        }

        // Inflate the layout for this fragment
        return view
    }

}