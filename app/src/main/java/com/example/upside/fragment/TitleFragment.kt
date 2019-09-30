package com.example.upside.fragment

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.upside.R

class TitleFragment : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val root = inflater.inflate(R.layout.title_screen, container, false)

        Thread(Runnable {
            Thread.sleep(2000)
            activity?.mainLooper.run {
                val transaction = fragmentManager?.beginTransaction()
                transaction?.replace(R.id.fl_main, DealsFragment())
                transaction?.addToBackStack(null)
                transaction?.commit()
            }
        }).start()

        return root
    }
}