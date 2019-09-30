package com.example.upside.fragment

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.upside.MainActivity
import com.example.upside.R
import com.example.upside.model.*
import com.example.upside.viewmodel.*
import com.google.gson.Gson
import com.squareup.picasso.Picasso


class DealsFragment : Fragment() {
    var dealsView : RecyclerView? = null
    var adapter : RecyclerView.Adapter<CustomViewHolder>? = null

    // view models
    var dealsViewModel: DealsViewModel? = null
    var airlinesViewModel: AirlinesViewModel? = null
    var hotelsViewModel: HotelsViewModel? = null
    var currenciesViewModel : CurrenciesViewModel? = null
    var airportsViewModel : AirportsViewModel? = null

    // models
    var deals = Deals(emptyList())
    var airlines = Airlines(emptyMap())
    var hotels = Hotels(emptyMap())
    var currencies = Currencies(emptyMap())
    var airports = Airports(emptyMap())

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        Log.e("DealsFragment", "onCreateView")

        val rootView = inflater.inflate(R.layout.fragment_deals, container, false)

        // create views here
        dealsView = rootView.findViewById(R.id.rv_deals)
        dealsView?.setLayoutManager(LinearLayoutManager(activity))
        dealsView?.setAdapter(adapter)

        return rootView
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.e("DealsFragment", "onCreate")

        // initialize view models
        dealsViewModel = ViewModelProviders.of(this).get(DealsViewModel::class.java)
        airlinesViewModel = ViewModelProviders.of(this).get(AirlinesViewModel::class.java)
        hotelsViewModel = ViewModelProviders.of(this).get(HotelsViewModel::class.java)
        currenciesViewModel = ViewModelProviders.of(this).get(CurrenciesViewModel::class.java)
        airportsViewModel = ViewModelProviders.of(this).get(AirportsViewModel::class.java)

        dealsViewModel?.initialize()
        airlinesViewModel?.initialize()
        hotelsViewModel?.initialize()
        currenciesViewModel?.initialize()
        airportsViewModel?.initialize()

        adapter = object : RecyclerView.Adapter<CustomViewHolder>() {
            override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CustomViewHolder {
                val view: View = LayoutInflater.from(parent.context).inflate(R.layout.deal, parent, false)

                val airlineImg : ImageView = view.findViewById(R.id.iv_airline)
                val hotelImg : ImageView = view.findViewById(R.id.iv_hotel)

                val fromText : TextView = view.findViewById(R.id.tv_from)
                val toText : TextView = view.findViewById(R.id.tv_to)
                val nightsText : TextView = view.findViewById(R.id.tv_nights)
                val priceText : TextView = view.findViewById(R.id.tv_price)

                return CustomViewHolder(view, airlineImg, hotelImg, fromText, toText, nightsText, priceText)
            }

            override fun getItemCount(): Int {
                return deals.list.size
            }

            override fun onBindViewHolder(holder: CustomViewHolder, position: Int) {
                val deal = deals.list[position]
                val hotel = hotels.map[deal.hotel.brand]
                val airline = airlines.map[deal.flights["outbound"]?.airline]
                val currency = currencies.map[deal.currency]

                Picasso.get().load(airline?.image_url).into(holder.airlineImg)
                Picasso.get().load(hotel?.image_url).into(holder.hotelImg)

                holder.fromText?.text = deal.flights["outbound"]?.start?.airport
                holder.toText?.text = deal.flights["outbound"]?.end?.airport
                holder.nightsText?.text = "${deal.hotel.nights} Nights"
                holder.priceText?.text = handleCurrencyText(deal.price, currency)

                holder.itemView.setOnClickListener {
                    val transaction = fragmentManager?.beginTransaction()
                    val currFragment = fragmentManager?.findFragmentById(R.id.fl_main)
                    val newFragment = DetailFragment()

                    val gson = Gson()

                    val bundle = Bundle() // this can be done with a Parcelable
                    bundle.putString("deal", gson.toJson(deal).toString())
                    bundle.putString("hotel", gson.toJson(hotel).toString())
                    bundle.putString("airline", gson.toJson(airline).toString())
                    bundle.putString("currency", gson.toJson(currency).toString())

                    //airport - need airport info but not all airports info
                    val outStart = airports.map[deal.flights.get("outbound")?.start?.airport]
                    val outEnd = airports.map[deal.flights.get("outbound")?.end?.airport]
                    val retStart = airports.map[deal.flights.get("return")?.start?.airport]
                    val retEnd = airports.map[deal.flights.get("return")?.end?.airport]
                    bundle.putString("out-start", gson.toJson(outStart))
                    bundle.putString("out-end", gson.toJson(outEnd).toString())
                    bundle.putString("ret-start", gson.toJson(retStart).toString())
                    bundle.putString("ret-end", gson.toJson(retEnd).toString())

                    newFragment.arguments = bundle

                    transaction?.detach(currFragment!!)
                    transaction?.replace(R.id.fl_main, newFragment, "details")
                    transaction?.commit()
                }
            }
        }
    }

    override fun onResume() {
        super.onResume()
        Log.e("DealsFragment", "onResume")
        observeViewModels(adapter!!)
    }

    fun notifyUpdate() {
        adapter?.notifyDataSetChanged()
    }

    private fun handleCurrencyText(price: Double, currency: Currency?) : String {
        if (MainActivity.selectedCurrency == null ||
            currency == MainActivity.selectedCurrency
        ) {
            return "${String.format("%.2f", price)} ${currency?.name}"
        }

        //convert price to dollars
        val pDols = if (currency?._id == "USD") {
            price
        } else {
            (price / currency?.exchange!!)
        }

        val pSel = (pDols * MainActivity.selectedCurrency?.exchange!!)

        return "${String.format("%.2f", pSel)} ${MainActivity.selectedCurrency?.name}"
    }

    private fun observeViewModels(adapter: RecyclerView.Adapter<CustomViewHolder>) {
        dealsViewModel?.deals!!.observe(this,
            Observer<Deals> {
                deals = it
                adapter.notifyDataSetChanged()
            }
        )
        airlinesViewModel?.airlines!!.observe(this,
            Observer<Airlines> {
                airlines = it
                adapter.notifyDataSetChanged()
            }
        )
        hotelsViewModel?.hotels!!.observe(this,
            Observer<Hotels> {
                hotels = it
                adapter.notifyDataSetChanged()
            }
        )
        currenciesViewModel?.currencies!!.observe( this,
            Observer<Currencies> {
                currencies = it
                adapter.notifyDataSetChanged()
            }
        )
        airportsViewModel?.airports!!.observe(this,
            Observer<Airports> {
                airports = it
            }
        )
    }

    class CustomViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var airlineImg : ImageView? = null
        var hotelImg : ImageView? = null

        var fromText : TextView? = null
        var toText : TextView? = null
        var nightsText : TextView? = null
        var priceText : TextView? = null

        constructor(itemView: View, airlineImg: ImageView, hotelImg: ImageView,
                    fromText: TextView, toText: TextView, nightsText: TextView, priceText: TextView) : this(itemView) {
            this.airlineImg = airlineImg
            this.hotelImg = hotelImg
            this.fromText = fromText
            this.toText = toText
            this.nightsText = nightsText
            this.priceText = priceText
        }
    }
}
