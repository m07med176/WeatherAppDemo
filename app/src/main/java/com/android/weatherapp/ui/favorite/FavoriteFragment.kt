package com.android.weatherapp.ui.favorite

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.viewmodel.viewModelFactory
import com.android.weatherapp.R
import com.android.weatherapp.data.Repository
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

class FavoriteFragment : Fragment() {

    private lateinit var viewModel: FavoriteViewModel
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_favorite,container,false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val repository = Repository.getInstance(requireActivity().application)
        val viewModelFactory = FavoriteViewModelFactory(repository = repository)
        viewModel = ViewModelProvider(requireActivity(),viewModelFactory).get(FavoriteViewModel::class.java)


        viewModel.getFavoriteList()

        lifecycleScope.launch{
            viewModel.favoriteList.collect{favoriteList->
                Toast.makeText(requireContext(), "length of data is: ${favoriteList.size}", Toast.LENGTH_SHORT).show()
            }
        }

    }
}