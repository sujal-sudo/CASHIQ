package com.example.yourapp

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import android.widget.TextView
import androidx.navigation.fragment.findNavController
import com.example.cashiq.R

class SettingsFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_settings, container, false)
    }
}
//
//    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
//        super.onViewCreated(view, savedInstanceState)
//
//        view.findViewById<TextView>(R.id.tv_currency).setOnClickListener {
//            // Navigate to Currency settings
//            findNavController().navigate(R.id.action_settingsFragment_to_currencyFragment)
//        }
//
//        view.findViewById<TextView>(R.id.tv_language).setOnClickListener {
//            // Navigate to Language settings
//            findNavController().navigate(R.id.action_settingsFragment_to_languageFragment)
//        }
//
//        view.findViewById<TextView>(R.id.tv_theme).setOnClickListener {
//            // Navigate to Theme settings
//            findNavController().navigate(R.id.action_settingsFragment_to_themeFragment)
//        }
//
//        view.findViewById<TextView>(R.id.tv_security).setOnClickListener {
//            // Navigate to Security settings
//            findNavController().navigate(R.id.action_settingsFragment_to_securityFragment)
//        }
//
//        view.findViewById<TextView>(R.id.tv_notification).setOnClickListener {
//            // Navigate to Notification settings
//            findNavController().navigate(R.id.action_settingsFragment_to_notificationFragment)
//        }
//
//        view.findViewById<TextView>(R.id.tv_about).setOnClickListener {
//            // Navigate to About
//            findNavController().navigate(R.id.action_settingsFragment_to_aboutFragment)
//        }
//
//        view.findViewById<TextView>(R.id.tv_help).setOnClickListener {
//            // Navigate to Help
//            findNavController().navigate(R.id.action_settingsFragment_to_helpFragment)
//        }
//    }
//}