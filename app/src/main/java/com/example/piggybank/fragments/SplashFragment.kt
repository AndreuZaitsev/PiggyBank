package com.example.piggybank.fragments

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.piggybank.R
import com.example.piggybank.fragments.common.BaseFragment
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.analytics.ktx.analytics
import com.google.firebase.ktx.Firebase
import com.google.firebase.ktx.app

class SplashFragment : BaseFragment(R.layout.splash_fragment) {

    override fun onCreate(savedInstanceState: Bundle?) {

        FirebaseAnalytics.getInstance(requireContext()).logEvent("log_event", null)
        injector.inject(this)
        super.onCreate(savedInstanceState)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        scheduleNavigationToMain()
    }

    private fun scheduleNavigationToMain() {
        val currentTime = System.currentTimeMillis()
        val isTimePassed = { (System.currentTimeMillis() - currentTime) >= DELAY_MILLIS }
        Thread {
            while (true) {
                if (isTimePassed()) {
                    break
                }
            }
            activity?.runOnUiThread {
                findNavController().navigate(R.id.action_splashFragment_to_mainFragment)
            }

        }.start()
    }

    companion object {

        private const val DELAY_MILLIS = 1000L
    }
}