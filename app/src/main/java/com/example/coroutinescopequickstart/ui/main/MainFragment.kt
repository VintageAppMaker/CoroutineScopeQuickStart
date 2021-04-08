package com.example.coroutinescopequickstart.ui.main

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.lifecycle.Observer
import com.example.coroutinescopequickstart.R

class MainFragment : Fragment() {

    companion object {
        fun newInstance() = MainFragment()
    }

    private lateinit var viewModel: MainViewModel

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View {

        viewModel = ViewModelProvider(this).get(MainViewModel::class.java)

        val v = inflater.inflate(R.layout.main_fragment, container, false)
        bindingObserver(v.findViewById(R.id.message), v.findViewById<LinearLayout>(R.id.lnMessage))
        return v
    }

    private fun bindingObserver(txt : TextView, ln :LinearLayout) {

        viewModel.imgList.observe(this, Observer {
            lst ->
            if (lst.size < 0) return@Observer

            lst.forEach { item ->
                ln.addView(TextView(activity!!.baseContext).apply { text = item })
            }
        })

        viewModel.status.observe(this, Observer {
            txt!!.text = it
        })
    }

}