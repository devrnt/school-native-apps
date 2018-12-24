package com.jonasdevrient.citypinboard.account


import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.jonasdevrient.citypinboard.R
import com.jonasdevrient.citypinboard.adapters.PostsAdapter
import com.jonasdevrient.citypinboard.responses.CheckGebruikersnaamResponse
import com.jonasdevrient.citypinboard.responses.PostResponse
import com.jonasdevrient.citypinboard.services.GebruikerService
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers


class AccountFragment : Fragment() {
    private lateinit var recyclerView: RecyclerView
    lateinit var viewAdapter: RecyclerView.Adapter<*>
    private lateinit var viewManager: RecyclerView.LayoutManager


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.account_fragment, container, false)

        activity?.actionBar?.setDisplayHomeAsUpEnabled(true)
        activity?.actionBar?.title = "Mijn likes"


        return view
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        getLikedPosts()
    }

    private fun getLikedPosts() {
        val call = GebruikerService.repository.getLikedPosts(CheckGebruikersnaamResponse("jonas2"))

        call.observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(this::handleResponse, this::handleError)
                .dispose()
    }


    private fun handleResponse(likedPosts: List<PostResponse>) {
        viewManager = LinearLayoutManager(context)
        viewAdapter = PostsAdapter(context!!, likedPosts.toMutableList())

        recyclerView = view!!.findViewById<RecyclerView>(R.id.recycler_view_posts).apply {
            setHasFixedSize(true)

            layoutManager = viewManager

            adapter = viewAdapter
        }
    }

    private fun handleError(error: Throwable) {
        // Get error as HTTPException to get the exception code
        print(error)
    }


}
