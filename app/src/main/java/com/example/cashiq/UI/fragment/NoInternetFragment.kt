package com.example.cashiq.UI.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.cashiq.R


/**
 * A simple [Fragment] subclass.
 * Use the [NoInternetFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class NoInternetFragment : Fragment() {


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_no_internet, container, false)
    }


}