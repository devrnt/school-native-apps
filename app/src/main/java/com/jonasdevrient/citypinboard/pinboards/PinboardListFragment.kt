package com.jonasdevrient.citypinboard.pinboards

import android.arch.lifecycle.ViewModelProviders
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.SearchView
import android.view.*
import android.widget.EditText
import android.widget.ProgressBar
import com.jonasdevrient.citypinboard.R
import com.jonasdevrient.citypinboard.adapters.PinboardsAdapter
import com.jonasdevrient.citypinboard.models.Pinboard
import com.jonasdevrient.citypinboard.viewmodels.PinboardViewModel
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.pinboard_list_fragment.view.*


class PinboardListFragment : Fragment() {
    private lateinit var pinboardViewModel: PinboardViewModel

    private lateinit var recyclerView: RecyclerView
    private lateinit var viewAdapter: RecyclerView.Adapter<*>
    private lateinit var viewManager: RecyclerView.LayoutManager
    private lateinit var pinboards: List<Pinboard>
    lateinit var displayPinboards: MutableList<Pinboard>
    private lateinit var spinner: ProgressBar

    private lateinit var searchItem: MenuItem
    private lateinit var accountItem: MenuItem


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
        pinboardViewModel = ViewModelProviders.of(this).get(PinboardViewModel::class.java)
    }


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment with the CityList theme
        val view = inflater.inflate(R.layout.pinboard_list_fragment, container, false)

        spinner = view.spinner_pinboards

        recyclerView = view.findViewById(R.id.recycler_view)
        viewManager = LinearLayoutManager(activity)

        loadPinboards()

        return view
    }

    // needed when user returns to the page
    override fun onResume() {
        super.onResume()
        loadPinboards()
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.search_menu, menu)
        searchItem = menu.findItem(R.id.search_menu)
        searchItem.isEnabled = false
        //accountItem = menu.findItem(R.id.account_menu)
        //accountItem.setOnMenuItemClickListener { navigateToList() }

        val searchView = searchItem.actionView as SearchView
        val editText = searchView.findViewById<EditText>(android.support.v7.appcompat.R.id.search_src_text)
        editText.hint = getString(R.string.hint_search)

        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                if (newText!!.isNotEmpty()) {
                    displayPinboards.clear()

                    val search = newText.toLowerCase()

                    pinboards.forEach {
                        if (it.city.toLowerCase().contains(search)) {
                            displayPinboards.add(it)
                        }
                    }
                    viewAdapter.notifyDataSetChanged()
                } else {
                    displayPinboards.clear()
                    displayPinboards.addAll(pinboards)
                    viewAdapter.notifyDataSetChanged()
                }
                return true
            }

        })

        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        (activity as AppCompatActivity).supportActionBar!!.show()
    }

    private fun loadPinboards() {
        pinboardViewModel.getAllPinboards()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(this::handleResponse, this::handleError)
    }

    private fun handleResponse(pinboards: List<Pinboard>) {

        this.pinboards = pinboards
        displayPinboards = pinboards.toMutableList()

        searchItem.isEnabled = true

        spinner.visibility = View.GONE

        viewAdapter = PinboardsAdapter(context, displayPinboards)

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
