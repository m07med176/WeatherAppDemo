package com.android.weatherapp.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.android.weatherapp.R
import com.android.weatherapp.data.ApiResponse
import com.android.weatherapp.data.Repository
import com.android.weatherapp.databinding.FragmentHomeBinding
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

class HomeFragment : Fragment() {

    lateinit var viewModel: HomeViewModel
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_home,container,false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val repository = Repository(requireContext())
        val homeViewModelFactory = HomeViewModelFactory(repository)
        viewModel = ViewModelProvider(requireActivity(),homeViewModelFactory).get(HomeViewModel::class.java)


        viewModel.getWeatherDetails(30.61554342119405, 32.27797547385768)


        lifecycleScope.launch{
            viewModel.weatherDetails.collect{state->
                when(state){
                    is ApiResponse.OnSuccess -> {
//                        Toast.makeText(requireContext(), state.data.toString(), Toast.LENGTH_SHORT).show()
                    }
                    is ApiResponse.OnError -> {
//                        Toast.makeText(requireContext(), state.message, Toast.LENGTH_SHORT).show()

                    }
                    is ApiResponse.OnLoading -> {
//                        Toast.makeText(requireContext(), "Loading...", Toast.LENGTH_SHORT).show()

                    }
                }
            }
        }

    }
}