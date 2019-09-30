package com.example.upside

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.example.upside.fragment.DealsFragment
import com.example.upside.fragment.DetailFragment
import com.example.upside.fragment.TitleFragment
import com.example.upside.model.Currencies
import com.example.upside.model.Currency
import com.example.upside.model.CurrencyDeserializer
import com.example.upside.viewmodel.CurrenciesViewModel
import com.google.android.material.floatingactionbutton.FloatingActionButton

class MainActivity : FragmentActivity() {
    companion object {
        var selectedCurrency : Currency? = null
    }

    var currencies : Currencies? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.fragment_main)

        val transaction = supportFragmentManager.beginTransaction()
        transaction.add(R.id.fl_main, TitleFragment(), "title")
        transaction.commit()

        val currenciesViewModel = ViewModelProviders.of(this).get(CurrenciesViewModel::class.java)
        currenciesViewModel.initialize()
        currenciesViewModel?.currencies!!.observe( this,
            Observer<Currencies> {
                currencies = it
            }
        )

        // to toggle the currency conversion into different formats
        val fab : FloatingActionButton = findViewById(R.id.fab)
        fab.setOnClickListener {
            Log.i("fab", "clicked")
            if (currencies != null) {
                val currNames = CurrencyDeserializer.currencyNames

                Log.i("curr", "$selectedCurrency")

                for (i in currNames.indices) {
                    val c = currencies!!.map[currNames[i]]
                    if (selectedCurrency == null) {
                        selectedCurrency = c
                        break
                    } else if (selectedCurrency?._id.equals(c?._id)) {
                        selectedCurrency = if ((i+1) < currNames.size) {
                            currencies!!.map[currNames[i + 1]]
                        } else {
                            currencies!!.map[currNames[0]]
                        }
                        break
                    }
                }

                Log.i("curr", "$selectedCurrency")

                // call update in fragment
                val currFragment = supportFragmentManager.findFragmentById(R.id.fl_main)
                if (currFragment is DealsFragment) {
                    currFragment.notifyUpdate()
                } else if (currFragment is DetailFragment) {
                    currFragment.notifyUpdate()
                }
            }
        }
    }

    override fun onBackPressed() {
        val transaction = supportFragmentManager.beginTransaction()
        val currFragment = supportFragmentManager.findFragmentById(R.id.fl_main)

        transaction.detach(currFragment!!)
        transaction.add(R.id.fl_main, DealsFragment(), "deals")
        transaction.commit()
    }
}