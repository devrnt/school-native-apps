package com.jonasdevrient.citypinboard

import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.jonasdevrient.citypinboard.adapters.PinboardsAdapter
import com.jonasdevrient.citypinboard.models.Pinboard
import com.jonasdevrient.citypinboard.repositories.PinboardAPI
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers


class PinboardListFragment : Fragment() {
    private lateinit var recyclerView: RecyclerView
    private lateinit var viewAdapter: RecyclerView.Adapter<*>
    private lateinit var viewManager: RecyclerView.LayoutManager
    private lateinit var pinboards: List<Pinboard>

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment with the CityList theme
        val view = inflater.inflate(R.layout.pinboard_list_fragment, container, false)


        recyclerView = view.findViewById(R.id.recycler_view)
        viewManager = LinearLayoutManager(activity)

        loadPinboards()


        return view
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        (activity as AppCompatActivity).supportActionBar!!.show()
    }

    private fun loadPinboards() {
        val call = PinboardAPI.repository
                .getAll()

        call.observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(this::handleResponse, this::handleError)
    }

    private fun handleResponse(pinboards: List<Pinboard>) {

        print(pinboards)
        viewAdapter = PinboardsAdapter(context, pinboards)

        recyclerView.apply {
            setHasFixedSize(true)

            layoutManager = viewManager
            adapter = viewAdapter
        }


    }

    private fun handleError(error: Throwable) {
        print(error)
    }

}
