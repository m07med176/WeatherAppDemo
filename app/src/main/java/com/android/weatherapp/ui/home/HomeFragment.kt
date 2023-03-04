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
import com.android.weatherapp.R
import com.android.weatherapp.data.Repository
import com.android.weatherapp.databinding.FragmentHomeBinding

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

        viewModel.weatherDetails.observe(viewLifecycleOwner){weather->
            Toast.makeText(requireContext(), weather.toString(), Toast.LENGTH_SHORT).show()
        }


    }
}