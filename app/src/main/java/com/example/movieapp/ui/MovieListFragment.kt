package com.example.movieapp.ui

import androidx.lifecycle.ViewModelProviders
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import com.example.movieapp.R
import com.example.movieapp.data.network.ErrorCode
import com.example.movieapp.data.network.Status
import kotlinx.android.synthetic.main.movie_list_fragment.*

class MovieListFragment : Fragment() {

    private lateinit var viewModel: MovieListViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.movie_list_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(MovieListViewModel::class.java)
        with(movie_list){
            adapter = MovieAdapter {
                findNavController().navigate(
                    MovieListFragmentDirections.actionMovieListFragmentToMovieDetailFragment(it)
                )
            }
        }

        viewModel.movies.observe(viewLifecycleOwner, Observer {
            (movie_list.adapter as MovieAdapter).submitList(it)
            if(it.isEmpty()){
                viewModel.fetchFromNetwork()
            }
        })

        viewModel.loadingStatus.observe(viewLifecycleOwner, Observer {
            when{
                it?.status == Status.LOADING -> {
                    loading_status.visibility = View.VISIBLE
                    status_error.visibility = View.INVISIBLE
                }
                it?.status == Status.SUCCESS -> {
                    loading_status.visibility = View.INVISIBLE
                    status_error.visibility = View.INVISIBLE
                }
                it?.status == Status.ERROR -> {
                    loading_status.visibility = View.INVISIBLE
                    showErrorMessage(it.errorCode, it.message)
                    status_error.visibility = View.VISIBLE
                }
            }
            swipe_refresh.isRefreshing = false
        })

        swipe_refresh.setOnRefreshListener {
            viewModel.refreshData()
        }

        viewModel.response.observe(viewLifecycleOwner, Observer {
            Log.d("MovieListFragment", it)
        })
    }

    private fun showErrorMessage(errorCode: ErrorCode?, message : String?){
        when(errorCode){
            ErrorCode.NO_DATA -> status_error.text = "No data returned from server. Please try again"
            ErrorCode.UNKNOWN_ERROR-> status_error.text = "Unknown error. $message"
            ErrorCode.NETWORK_ERROR-> status_error.text = "Error fetching data. Please check your network."
        }
    }

}