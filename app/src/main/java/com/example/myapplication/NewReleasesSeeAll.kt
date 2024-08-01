package com.example.myapplication

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.Recycler
import kotlinx.coroutines.launch

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [NewReleasesSeeAll.newInstance] factory method to
 * create an instance of this fragment.
 */
class NewReleasesSeeAll : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null
    private lateinit var recyclerView : RecyclerView
    private lateinit var customAdapter: CustomAdapter
    private lateinit var  imageList : ArrayList<ImageLoad>



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_new_releases_see_all, container, false)

        recyclerView = view.findViewById(R.id.recyclerView)
        recyclerView.layoutManager = GridLayoutManager(context, 2)
        recyclerView.hasFixedSize()

        imageList = arrayListOf<ImageLoad>()

        customAdapter = CustomAdapter(imageList, requireContext())
        recyclerView.adapter = customAdapter

        val movieViewModel: MovieViewModel by viewModels {
            ViewModelProvider.AndroidViewModelFactory.getInstance(requireActivity().application)
        }

        viewLifecycleOwner.lifecycleScope.launch {
            movieViewModel.newMovieStateFlow.collect { movieResponse ->
                movieResponse?.let {
                    // Handle successful response
                    customAdapter.onSuccessPopulate(movieResponse)

                } ?: run {
                    // Handle null response or error state
                    Log.d("NewReleaseSeeAll", "Error in API call or null response")
                }
            }
        }

        movieViewModel.fetchUpcomingMovies()
        return view
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment NewReleasesSeeAll.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            NewReleasesSeeAll().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}